package backend.backend.controller;

import backend.backend.component.AuthServiceFactory;
import backend.backend.domain.User;
import backend.backend.dto.auth.request.KakaoLoginRequest;
import backend.backend.dto.auth.request.KakaoSignupRequest;
import backend.backend.dto.auth.request.LocalLoginRequest;
import backend.backend.dto.auth.request.LocalSignupRequest;
import backend.backend.dto.auth.response.*;
import backend.backend.exception.AuthException;
import backend.backend.security.jwt.JwtUtils;
import backend.backend.service.AuthService;
import backend.backend.service.LocalAuthService;
import backend.backend.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServiceFactory authServiceFactory;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody LocalSignupRequest request) {
        AuthService<LocalLoginRequest, LocalSignupRequest> authService = authServiceFactory.getLocalAuthService();
        authService.signup(request);
        return ResponseEntity.ok(SignupResponse.builder().message("회원가입이 완료되었습니다.").build());
    }

    @PostMapping("/kakaoSignup")
    public ResponseEntity<KakaoSignupResponse> kakaoSignup(@RequestBody KakaoSignupRequest request) {
        AuthService<KakaoLoginRequest, KakaoSignupRequest> authService = authServiceFactory.getKakaoAuthService();

        authService.signup(request);
        return ResponseEntity.ok(KakaoSignupResponse.builder().message("회원가입이 완료되었습니다.").build());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login (@Valid @RequestBody LocalLoginRequest request, HttpServletResponse response) {
        AuthService<LocalLoginRequest, LocalSignupRequest> authService = authServiceFactory.getLocalAuthService();
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

    @PostMapping("/kakaoLogin")
    public ResponseEntity<KakaoLoginResponse> kakaoLogin(@RequestBody KakaoLoginRequest request, HttpServletResponse response) {
        AuthService<KakaoLoginRequest, KakaoSignupRequest> authService = authServiceFactory.getKakaoAuthService();
        String accessToken = authService.login(request);

        KakaoLoginResponse kakaoLoginResponse = KakaoLoginResponse.builder()
                .accessToken(accessToken)
                .message("반갑습니다 " + jwtUtils.getUserNameFromToken(accessToken) + "님")
                .build();

        Cookie refreshTokenCookie = new Cookie("refreshToken", jwtUtils.generateRefreshToken(jwtUtils.getUserEmailFromToken(accessToken)));
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(kakaoLoginResponse);
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
                .orElseThrow(() -> new AuthException("refreshToken을 찾지 못했습니다."));
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
        throw new AuthException("refreshToken 검증에 실패했습니다.");
    }
    @GetMapping("/validate")
    public ResponseEntity<TokenValidationResponse> getMyInfo(@AuthenticationPrincipal String email) {
        return ResponseEntity.ok(TokenValidationResponse.builder().message("유효한 토큰입니다.").build());
    }
}
