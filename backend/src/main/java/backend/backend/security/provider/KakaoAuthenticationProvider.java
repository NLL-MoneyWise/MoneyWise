package backend.backend.security.provider;

import backend.backend.domain.User;
import backend.backend.dto.auth.response.KakaoTokenApiResponse;
import backend.backend.dto.auth.response.KakaoUserInfoApiResponse;
import backend.backend.exception.*;
import backend.backend.repository.UserRepository;
import backend.backend.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataAccessException;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoAuthenticationProvider extends OAuth2AuthenticationProvider {
    private final RestTemplate restTemplate;

    @Value("${kakao.client.id}")
    private String kakaoClientId;

    @Value("${kakao.redirect.uri}")
    private String kakaoRedirectUri;

    public KakaoAuthenticationProvider(UserRepository userRepository, JwtUtils jwtUtils, RestTemplate restTemplate) {
        super(userRepository, jwtUtils);
        this.restTemplate = restTemplate;
    }

    @Override
    protected User authenticationUser(String code) {
        KakaoTokenApiResponse kakaoTokenApiResponse = getKakaoTokenResponse(code);
        KakaoUserInfoApiResponse kakaoUserInfoApiResponse = getKakaoUserInfo(kakaoTokenApiResponse.getAccess_token());
        Long kakaoUserId = kakaoUserInfoApiResponse.getId();
        String kakaoUserNickname = kakaoUserInfoApiResponse.getProperties().getNickname();


        User user;

        if (!userRepository.existsByProviderAndProviderId("kakao", kakaoUserId)) {
            String email = kakaoClientId + "@kakao.auth";

            user = new User();
            user.setProvider("kakao");
            user.setProviderId(kakaoUserId);
            user.setName(kakaoUserNickname);
            user.setNickname(kakaoUserNickname);
            user.setEmail(email);

            try {
                userRepository.save(user);
            } catch (DataAccessException e) {
                throw new DatabaseException("회원가입에 실패했습니다.");
            }
        } else {
                user = userRepository.findByProviderId(kakaoUserId)
                        .orElseThrow(() -> new NotFoundException("해당하는 유저를 찾을 수 없습니다."));
        }

        return user;
    }

    @Override
    public String getProviderType() {
        return "kakao";
    }

    private KakaoTokenApiResponse getKakaoTokenResponse(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId);
        body.add("redirect_uri", kakaoRedirectUri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<KakaoTokenApiResponse> response = restTemplate.exchange(
                    tokenUrl,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<KakaoTokenApiResponse>() {
                    }
            );

            return response.getBody();

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new ValidationException("잘못된 요청입니다.");
            }
            throw new APIException("카카오 API호출에 실패했습니다.");
        } catch (ResourceAccessException e) {
            throw new NetworkException("카카오 서버에 연결할 수 없습니다.");
        }
    }

    private KakaoUserInfoApiResponse getKakaoUserInfo(String access_token) {
        try {
            String bearerToken = "Bearer " + access_token;

            String infoUrl = "https://kapi.kakao.com/v2/user/me";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", bearerToken);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("property_keys", "[\"properties.nickname\"]");

            HttpEntity<MultiValueMap<String, String>> requestEntity =
                    new HttpEntity<>(body, headers);

            ResponseEntity<KakaoUserInfoApiResponse> response = restTemplate.exchange(
                    infoUrl,
                    HttpMethod.POST,
                    requestEntity,
                    new ParameterizedTypeReference<KakaoUserInfoApiResponse>() {
                    }
            );

            return response.getBody();

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new AuthException("유효하지 않은 access_token입니다.");
            } else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new ValidationException("잘못된 요청입니다.");
            }
            throw new APIException("카카오 API호출에 실패했습니다.");
        } catch (ResourceAccessException e) {
            throw new NetworkException("카카오 서버에 연결할 수 없습니다.");
        }
    }
}
