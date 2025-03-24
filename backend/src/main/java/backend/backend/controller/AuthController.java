package backend.backend.controller;

import backend.backend.component.AuthServiceFactory;
import backend.backend.domain.User;
import backend.backend.dto.auth.request.KakaoLoginRequest;
import backend.backend.dto.auth.request.KakaoSignupRequest;
import backend.backend.dto.auth.request.LocalLoginRequest;
import backend.backend.dto.auth.request.LocalSignupRequest;
import backend.backend.dto.auth.response.*;
import backend.backend.exception.AuthException;
import backend.backend.exception.response.ErrorResponse;
import backend.backend.security.jwt.JwtUtils;
import backend.backend.service.AuthService;
import backend.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Tag(name = "Authentication", description = "인증 관련 기능")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServiceFactory authServiceFactory;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @Operation(summary = "로컬 회원 가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입이 완료되었습니다.",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SignupResponse.class),
                    examples = @ExampleObject("{\n" +
                            "\"message\": \"회원가입이 완료되었습니다.\"\n" +
                            "}"))),

            @ApiResponse(responseCode = "409", description = "이미 가입된 이메일 입니다.",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples = @ExampleObject(value = "{\n" +
                            "\"typeName\": \"CONFLICT_ERROR\",\n" +
                            "\"message\": \"이미 가입된 이메일 입니다.\"\n" +
                            "}"))),

            @ApiResponse(responseCode = "500", description = "사용자 정보 저장 중 오류가 발생했습니다.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class),
            examples = @ExampleObject(value = "{\n\"typeName\": \"DATABASE_ERROR\",\n\"message\": \"사용자 정보 저장 중 오류가 발생했습니다.\"\n}")))
    })
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody LocalSignupRequest request) {
        AuthService<LocalLoginRequest, LocalSignupRequest> authService = authServiceFactory.getLocalAuthService();
        authService.signup(request);
        return ResponseEntity.ok(SignupResponse.builder().message("회원가입이 완료되었습니다.").build());
    }

    @Operation(summary = "카카오 회원 가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입이 완료되었습니다.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = KakaoSignupResponse.class),
            examples = @ExampleObject("{\n\"message\": \"회원가입이 완료되었습니다.\"\n}"))),

            @ApiResponse(responseCode = "401", description = "유효하지 않은 카카오 ID 형식입니다.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class),
            examples = @ExampleObject("{\n\"typeName\": \"AUTH_ERROR\",\n\"message\": \"유효하지 않은 카카오 ID 형식입니다.\"\n}"))),

            @ApiResponse(responseCode = "409", description = "이미 가입된 이메일 입니다.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class),
            examples = @ExampleObject("{\n\"typeName\": \"CONFLICT_ERROR\",\n\"message\": \"이미 가입된 이메일 입니다.\"\n}"))),

            @ApiResponse(responseCode = "500", description = "사용자 정보 저장 중 오류가 발생했습니다.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class),
            examples = @ExampleObject(value = "{\n\"typeName\": \"DATABASE_ERROR\",\n\"message\": \"사용자 정보 저장 중 오류가 발생했습니다.\"\n}")))
    })
    @PostMapping("/kakaoSignup")
    public ResponseEntity<KakaoSignupResponse> kakaoSignup(@RequestBody KakaoSignupRequest request) {
        AuthService<KakaoLoginRequest, KakaoSignupRequest> authService = authServiceFactory.getKakaoAuthService();

        authService.signup(request);
        return ResponseEntity.ok(KakaoSignupResponse.builder().message("회원가입이 완료되었습니다.").build());
    }

    @Operation(summary = "로컬 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반갑습니다 test님",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = LocalLoginResponse.class),
            examples = @ExampleObject("{\n" +
                    "\"accessToken\": \"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QG5hdmVyLmNvbSIsIm5hbWUiOiLthYzsiqTtirgiLCJuaWNrTmFtZSI6Iu2FjOyKpO2KuOuLieuEpOyehCIsImV4cCI6MTczOTEwODQ4MH0.ZaMBUEGyEf0vs2ebstGvIiBH0AhE2eWLBacn7Ex1TRA\",\n" +
                    "\"message\": \"반갑습니다 test님\"\n" +
                    "}"))),

            @ApiResponse(responseCode = "401", description = "잘못된 비밀번호 입니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\n" +
                                    "\"typeName\": \"AUTH_ERROR\",\n" +
                                    "\"message\": \"잘못된 비밀번호 입니다.\"\n" +
                                    "}"))),

            @ApiResponse(responseCode = "404", description = "가입되지 않은 이메일 입니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\n\"typeName\": \"NOT_FOUND_ERROR\",\n\"message\": \"가입되지 않은 이메일 입니다.\"\n}")))
    })
    @PostMapping("/login")
    public ResponseEntity<LocalLoginResponse> login (@Valid @RequestBody LocalLoginRequest request, HttpServletResponse response) {
        AuthService<LocalLoginRequest, LocalSignupRequest> authService = authServiceFactory.getLocalAuthService();
        String accessToken = authService.login(request);
        LocalLoginResponse loginResponse = LocalLoginResponse.builder()
                .accessToken(accessToken)
                .message("반갑습니다 " + jwtUtils.getUserNameFromToken(accessToken) + "님").build();

        Cookie refreshTokenCookie = new Cookie("refreshToken", jwtUtils.generateRefreshToken(request.getEmail()));
        refreshTokenCookie.setHttpOnly(true); //JavaScript에서 접근 불가능하게 설정
        refreshTokenCookie.setSecure(false); //HTTPS에서만 전송되도록 설정
        refreshTokenCookie.setPath("/"); //모든 경로에서 쿠키 사용
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); //7일을 초로 변환

        response.addCookie(refreshTokenCookie);

        //return ResponseEntity.ok(loginResponse);
        //두 방식은 동일한 결과임
        return ResponseEntity.status(HttpStatus.OK)
                .body(loginResponse);
    }

    @Operation(summary = "카카오 로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반갑습니다 test님",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = KakaoLoginResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "\"accessToken\": \"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QG5hdmVyLmNvbSIsIm5hbWUiOiLthYzsiqTtirgiLCJuaWNrTmFtZSI6Iu2FjOyKpO2KuOuLieuEpOyehCIsImV4cCI6MTczOTEwODQ4MH0.ZaMBUEGyEf0vs2ebstGvIiBH0AhE2eWLBacn7Ex1TRA\",\n" +
                                    "\"email\": \"test@naver.com\",\n" +
                                    "\"message\": \"반갑습니다 test님\"\n" +
                                    "}"))),

            @ApiResponse(responseCode = "401", description = "카카오 사용자 id가 비어있습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "ID누락", value = "{\n\"typeName\": \"AUTH_ERROR\",\n\"message\": \"카카오 사용자 id가 비어있습니다.\"\n}"),
                                    @ExampleObject(name = "nickName누락", value = "{\n\"typeName\": \"AUTH_ERROR\",\n\"message\": \"카카오 사용자 nickName이 비어있습니다.\"\n}")
                            })),

            @ApiResponse(responseCode = "404", description = "추가정보 입력이 필요합니다. kakaoId: kakaoUserId, nickname: userNickName",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("{\n\"typeName\": \"NOT_FOUND_ERROR\",\n\"message\": \"추가정보 입력이 필요합니다. kakaoId: kakaoUserId, nickname: userNickName\"\n}"))),

            @ApiResponse(responseCode = "404", description = "추가정보 입력이 필요합니다. kakaoId: kakaoUserId, nickname: userNickName",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("{\n\"typeName\": \"NOT_FOUND_ERROR\",\n\"message\": \"추가정보 입력이 필요합니다. kakaoId: kakaoUserId, nickname: userNickName\"\n}")))

    })
    @PostMapping("/kakaoLogin")
    public ResponseEntity<KakaoLoginResponse> kakaoLogin(@RequestBody KakaoLoginRequest request, HttpServletResponse response) {
        AuthService<KakaoLoginRequest, KakaoSignupRequest> authService = authServiceFactory.getKakaoAuthService();
        String accessToken = authService.login(request);

        KakaoLoginResponse kakaoLoginResponse = KakaoLoginResponse.builder()
                .accessToken(accessToken)
                .email(jwtUtils.getUserEmailFromToken(accessToken))
                .message("반갑습니다 " + jwtUtils.getUserNameFromToken(accessToken) + "님")
                .build();

        Cookie refreshTokenCookie = new Cookie("refreshToken", jwtUtils.generateRefreshToken(jwtUtils.getUserEmailFromToken(accessToken)));
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(kakaoLoginResponse);
    }

    @Operation(summary = "액세스 토큰 재발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "accessToken이 재발급 되었습니다.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = TokenResponse.class),
            examples = @ExampleObject("{\n" +
                    "\"accessToken\": \"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QG5hdmVyLmNvbSIsIm5hbWUiOiLthYzsiqTtirgiLCJuaWNrTmFtZSI6Iu2FjOyKpO2KuOuLieuEpOyehCIsImV4cCI6MTczOTEwODQ4MH0.ZaMBUEGyEf0vs2ebstGvIiBH0AhE2eWLBacn7Ex1TRA\",\n" +
                    "\"message\": \"accessToken이 재발급 되었습니다.\"\n" +
                    "}"))),

            @ApiResponse(responseCode = "401", description = "refreshToken을 찾지 못했습니다.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class),
            examples = {
                    @ExampleObject(name = "refreshToken이 없음", value = "{\n" +
                            "\"typeName\": \"AUTH_ERROR\",\n" +
                            "\"message\": \"refreshToken을 찾지 못했습니다.\"\n" +
                            "}"),
                    @ExampleObject(name = "refreshToken은 있지만 검증에 실패", value = "{\n" +
                            "\"typeName\": \"AUTH_ERROR\",\n" +
                            "\"message\": \"refreshToken 검증에 실패했습니다.\"\n" +
                            "}")
                    }
            ))
    })
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


    @Operation(summary = "accessToken이 유효한 토큰인지 확인", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유효한 토큰입니다.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = TokenValidationResponse.class),
            examples = @ExampleObject("{\n" +
                    "\"message\": \"유효한 토큰입니다.\"\n" +
                    "}")))
    })
    @GetMapping("/validate")
    public ResponseEntity<TokenValidationResponse> getMyInfo(@AuthenticationPrincipal String email) {
        return ResponseEntity.ok(TokenValidationResponse.builder().message("유효한 토큰입니다.").build());
    }
}
