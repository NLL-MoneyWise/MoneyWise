package backend.backend.service;

import backend.backend.domain.User;
import backend.backend.dto.request.LoginRequest;
import backend.backend.dto.response.LoginResponse;
import backend.backend.repository.UserRepository;
import backend.backend.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public LoginResponse login(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if(!optionalUser.isPresent()) {
            throw new IllegalArgumentException("가입되지 않은 이메일 입니다.");
        }

        User user = optionalUser.get();
        //아래는 다른 표현식
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일 입니다."));
        //IllegalArgumentException = 메소드로 전달된 인자가 유효하지 않을 때 발생
        //orElseThrow는 optional객체가 null인 경우 예외를 발생시킨다. (optional도 자동으로 벗겨준다.)

        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }

        //JWT accessToken생성
        String accessToken = jwtUtils.generateAccessToken(user.getEmail());
        //JJWT빌더는 setExpiration처럼 사용하고 Lombok의 빌더는 필드명만 사용하여 set구현함

        return LoginResponse.of(accessToken, user);
    }
}
