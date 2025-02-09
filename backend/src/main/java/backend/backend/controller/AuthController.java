package backend.backend.controller;

import backend.backend.domain.User;
import backend.backend.dto.request.LoginRequest;
import backend.backend.dto.request.SignupRequest;
import backend.backend.dto.response.LoginResponse;
import backend.backend.dto.response.SignupResponse;
import backend.backend.dto.response.TokenResponse;
import backend.backend.dto.response.TokenValidationResponse;
import backend.backend.exception.AuthenticationException;
import backend.backend.security.jwt.JwtUtils;
import backend.backend.service.AuthService;
import backend.backend.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.ok(SignupResponse.builder().message("회원가입이 완료되었습니다.").build());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login (@RequestBody LoginRequest request, HttpServletResponse response) {
        String accessToken = authService.login(request);
        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(accessToken)
                .message("반갑습니다 " + jwtUtils.getUserNameFromToken(accessToken) + "님").build();

        Cookie refreshTokenCookie = new Cookie("refreshToken", jwtUtils.generateRefreshToken(request.getEmail()));
        refreshTokenCookie.setHttpOnly(true); //JavaScript에서 접근 불가능하게 설정
        refreshTokenCookie.setSecure(true); //HTTPS에서만 전송되도록 설정
        refreshTokenCookie.setPath("/"); //모든 경로에서 쿠키 사용
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); //7일을 초로 변환

        response.addCookie(refreshTokenCookie);

        //return ResponseEntity.ok(loginResponse);
        //두 방식은 동일한 결과임
        return ResponseEntity.status(HttpStatus.OK)
                .body(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        //refreshToken을 Cookie에서 찾는 2가지 방식
        //1방식
        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("refrechToken"))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthenticationException("refreshToken을 찾지 못했습니다."));
        //2방식
//        Cookie[] cookies = request.getCookies();
//        String refreshToken = null;
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals("refreshToken")) {
//                refreshToken = cookie.getValue();
//                break;
//            }
//        }
//        if (refreshToken == null) {
//            throw new IllegalArgumentException("Refresh token not found");
//        }

        if(jwtUtils.validateToken(refreshToken)) {
            String email = jwtUtils.getUserEmailFromToken(refreshToken);
            User user = userService.findByEmail(email);
            String accessToken = jwtUtils.generateAccessToken(email, user.getName(), user.getNickname());

            return ResponseEntity.ok(TokenResponse.builder().accessToken(accessToken)
                    .message("accessToken이 재발급 되었습니다.").build());
        }
        throw new AuthenticationException("refreshToken검증에 실패했습니다.");
    }
    @GetMapping("/validate")
    public ResponseEntity<TokenValidationResponse> getMyInfo(@AuthenticationPrincipal String email) {
        return ResponseEntity.ok(TokenValidationResponse.builder().message("유효한 토큰입니다.").build());
    }
}
