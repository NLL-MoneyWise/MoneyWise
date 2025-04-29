package backend.backend.service;

import backend.backend.domain.User;
import backend.backend.dto.auth.request.KakaoLoginRequest;
import backend.backend.dto.auth.request.LocalLoginRequest;
import backend.backend.dto.auth.request.LocalSignupRequest;
import backend.backend.dto.auth.response.LoginResponse;
import backend.backend.exception.*;
import backend.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ObjectMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(String provider, Object request) {
        Authentication authentication;

        try {
            if (provider.equals("local")) {
                LocalLoginRequest loginRequest = mapper.convertValue(request, LocalLoginRequest.class);

                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getEmail(),
                                loginRequest.getPassword()
                        )
                );
            } else if (provider.equals("kakao")) {
                KakaoLoginRequest kakaoLoginRequest = mapper.convertValue(request, KakaoLoginRequest.class);

                authentication = authenticationManager.authenticate(
                        new PreAuthenticatedAuthenticationToken(
                                provider,
                                kakaoLoginRequest.getCode()
                        )
                );
            } else {
                throw new ValidationException("지원하지 않는 로그인 방식입니다.");
            }

            LoginResponse response = (LoginResponse) authentication.getDetails();

            return response;
        } catch (BadCredentialsException e) {
            throw new AuthException(e.getMessage());
        } catch (Exception e) {
            throw new APIException("서버 내부 오류 발생");
        }
    }

    public void localSignup(LocalSignupRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("이미 존재하는 이메일 입니다.");
        }

        String password = passwordEncoder.encode(request.getPassword());

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setProvider("local");
        user.setNickname(request.getNickname());
        user.setPassword(password);
        try {
            userRepository.save(user);
        } catch (DataAccessException e) {
            throw new DatabaseException("회원가입에 실패했습니다.");
        }
    }

    public void deleteUser(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new NotFoundException("존재하지 않는 사용자 입니다.");
        }

        try {
            userRepository.deleteById(email);
        } catch (DataAccessException e) {
            throw new DatabaseException("사용자 삭제에 실패했습니다.");
        }
    }
}
