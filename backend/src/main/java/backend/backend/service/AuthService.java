package backend.backend.service;

import backend.backend.domain.User;
import backend.backend.dto.auth.request.LoginRequest;
import backend.backend.dto.auth.request.SignupRequest;
import backend.backend.exception.AuthException;
import backend.backend.exception.ConflictException;
import backend.backend.exception.DatabaseException;
import backend.backend.exception.NotFoundException;
import backend.backend.repository.UserRepository;
import backend.backend.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public String login(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if(!optionalUser.isPresent()) {
            throw new NotFoundException("가입되지 않은 이메일 입니다.");
        }

        User user = optionalUser.get();
        //아래는 다른 표현식
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일 입니다."));
        //IllegalArgumentException = 메소드로 전달된 인자가 유효하지 않을 때 발생
        //orElseThrow는 optional객체가 null인 경우 예외를 발생시킨다. (optional도 자동으로 벗겨준다.)


        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException("잘못된 비밀번호 입니다.");
        }

        //JWT accessToken생성
        String accessToken = jwtUtils.generateAccessToken(user.getEmail(), user.getName(), user.getNickname());
        //JJWT빌더는 setExpiration처럼 사용하고 Lombok의 빌더는 필드명만 사용하여 set구현함

        return accessToken;
    }

    public void signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("이미 가입된 이메일 입니다.");
        }

        String encodePassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodePassword);

        User user = new User();
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException | JpaSystemException e) {
            throw new DatabaseException("데이터베이스 저장 중 오류가 발생했습니다." + e.getMessage());
        }
    }
}
