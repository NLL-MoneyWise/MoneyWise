package backend.backend.service;

import backend.backend.domain.User;
import backend.backend.dto.request.LoginRequest;
import backend.backend.dto.request.SignupRequest;
import backend.backend.dto.response.LoginResponse;
import backend.backend.exception.LoginException;
import backend.backend.exception.SignupException;
import backend.backend.repository.UserRepository;
import backend.backend.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if(!optionalUser.isPresent()) {
            throw new LoginException("가입되지 않은 이메일 입니다.");
        }

        User user = optionalUser.get();
        //아래는 다른 표현식
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일 입니다."));
        //IllegalArgumentException = 메소드로 전달된 인자가 유효하지 않을 때 발생
        //orElseThrow는 optional객체가 null인 경우 예외를 발생시킨다. (optional도 자동으로 벗겨준다.)


        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new LoginException("잘못된 비밀번호 입니다.");
        }

        //JWT accessToken생성
        String accessToken = jwtUtils.generateAccessToken(user.getEmail());
        //JJWT빌더는 setExpiration처럼 사용하고 Lombok의 빌더는 필드명만 사용하여 set구현함

        return LoginResponse.of(accessToken, user);
    }

    public LoginResponse.UserInfo signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new SignupException("이미 가입된 이메일 입니다.");
        }
        String encodePassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodePassword);

        User user = new User();
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        userRepository.save(user);

        return LoginResponse.UserInfo.from(user);
    }
}
