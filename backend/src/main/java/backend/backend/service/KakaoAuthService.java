package backend.backend.service;

import backend.backend.domain.User;
import backend.backend.dto.auth.request.KakaoLoginRequest;
import backend.backend.dto.auth.request.KakaoSignupRequest;
import backend.backend.dto.auth.response.KakaoTokenApiResponse;
import backend.backend.dto.auth.response.KakaoUserInfoApiResponse;
import backend.backend.exception.*;
import backend.backend.repository.UserRepository;
import backend.backend.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Service("kakao")
@RequiredArgsConstructor
@Transactional
public class KakaoAuthService implements AuthService<KakaoLoginRequest, KakaoSignupRequest> {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final JwtUtils jwtUtils;

    @Value("${kakao.client.id}")
    private String kakaoClientId;
    @Value("${kakao.redirect.uri}")
    private String kakaoRedirectUri;

    @Override
    public String login(KakaoLoginRequest request) {
        KakaoTokenApiResponse kakaoTokenApiResponse = getKakaoTokenResponse(request.getCode());
        KakaoUserInfoApiResponse kakaoUserInfoApiResponse = getKakaoUserInfo(kakaoTokenApiResponse.getAccess_token());
        Long kakaoUserId = kakaoUserInfoApiResponse.getId();
        if (kakaoUserId == null) {
            throw new AuthException("카카오 사용자 id가 비어있습니다.");
        }

        String nickName = kakaoUserInfoApiResponse.getProperties().getNickName();
        if (nickName == null) {
            throw new AuthException("카카오 사용자 nickname이 비어있습니다.");
        }

        User user = userRepository.findByKakaoId(kakaoUserId)
                .orElseThrow(() -> new NotFoundException("추가정보 입력이 필요합니다. kakaoId: " + kakaoUserId + ", nickname: " + nickName));

        String jwtAccessToken = jwtUtils.generateAccessToken(user.getEmail(), user.getName(), user.getNickname());

        return jwtAccessToken;
    }

    @Override
    public void signup(KakaoSignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("이미 가입된 이메일 입니다.");
        }

        Long kakaoId = null;

        try {
            kakaoId = Long.parseLong(request.getKakaoId());
        } catch (NumberFormatException e) {
            throw new AuthException("유효하지 않은 카카오 ID 형식입니다.");
        }

        User user = new User();
        user.setKakaoId(kakaoId);
        user.setName(request.getName());
        user.setProvider("kakao");
        user.setNickname(request.getNickName());
        user.setEmail(request.getEmail());

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException | JpaSystemException e) {
            throw new DatabaseException("유저 정보 저장 중 오류가 발생했습니다.");
        }
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
                throw new AuthException("잘못된 요청입니다.");
            }
            throw new AuthException("카카오 API호출에 실패했습니다.");
        } catch (ResourceAccessException e) {
            throw new NetworkException("카카오 서버에 연결할 수 없습니다.");
        }
    }

    private KakaoUserInfoApiResponse getKakaoUserInfo(String accessToken) {
        try {
            String bearerToken = "Bearer " + accessToken;

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
                throw new AuthException("유효하지 않은 accessToken입니다." + accessToken);
            } else if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new AuthException("잘못된 요청입니다.");
            }
            throw new AuthException("카카오 API호출에 실패했습니다.");
        } catch (ResourceAccessException e) {
            throw new NetworkException("카카오 서버에 연결할 수 없습니다.");
        }
    }
}
