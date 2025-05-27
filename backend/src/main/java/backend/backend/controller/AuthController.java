package backend.backend.controller;

import backend.backend.domain.User;
import backend.backend.dto.auth.request.AccessTokenValidationRequest;
import backend.backend.dto.auth.request.LocalSignupRequest;
import backend.backend.dto.auth.response.*;
import backend.backend.exception.AuthException;
import backend.backend.exception.response.ErrorResponse;
import backend.backend.security.jwt.JwtUtils;
import backend.backend.service.AuthService;
import backend.backend.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Tag(name = "Authentication", description = "인증 관련 기능")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
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
        authService.localSignup(request);
        return ResponseEntity.ok(SignupResponse.builder().message("회원가입이 완료되었습니다.").build());
    }

    @Operation(summary = "통합 로그인 / provider: local, kakao")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반갑습니다 test님",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "access_token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QG5hdmVyLmNvbSIsIm5hbWUiOiLthYzsiqTtirgiLCJuaWNrTmFtZSI6Iu2FjOyKpO2KuOuLieuEpOyehCIsImV4cCI6MTczOTEwODQ4MH0.ZaMBUEGyEf0vs2ebstGvIiBH0AhE2eWLBacn7Ex1TRA",
                                    "email": "test@naver.com",
                                    "name": "test",
                                    "nickname": "nickname",
                                    "message": "반갑습니다 test님"
                                    }
                                    """))),

            @ApiResponse(responseCode = "400", description = "kakao: 서버 내부에서 카카오 API요청 형식이 잘못되었을 때",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "VALIDATION_ERROR",
                                    "message": "잘못된 요청입니다."
                                    }
                                    """))),

            @ApiResponse(responseCode = "401", description = """
                    잘못된 비밀번호 입니다. / 이메일을 찾을 수 없습니다. / 유효하지 않은 access_token입니다.
                    """,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "local: 비밀번호가 잘못된 경우", value = """
                                            {
                                            "typeName": "AUTH_ERROR",
                                            "message": "잘못된 비밀번호 입니다."
                                            }
                                            """),
                                    @ExampleObject(name = "local: 이메일이 틀린 경우", value = """
                                            {
                                            "typeName": "AUTH_ERROR",
                                            "message": "이메일을 찾을 수 없습니다."
                                            }
                                            """),
                                    @ExampleObject(name = "kakao: 카카오 액세스 토큰으로 유저 정보를 받아오는 중 액세스 토큰이 유효하지 않을 경우", value = """
                                            {
                                            "typeName": "AUTH_ERROR",
                                            "message": "유효하지 않은 access_token입니다."
                                            }
                                            """)
                            })),

            @ApiResponse(responseCode = "404", description = """
                    가입되지 않은 이메일 입니다. / 해당하는 유저를 찾을 수 없습니다.
                    """,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\n\"typeName\": \"NOT_FOUND_ERROR\",\n\"message\": \"가입되지 않은 이메일 입니다.\"\n}"))),

            @ApiResponse(responseCode = "500", description = "회원가입에 실패했습니다./카카오 API호출에 실패했습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "kakao: 자동 회원가입 오류", value = """
                                            {
                                            "typeName": "DATABASE_ERROR",
                                            "message": "회원가입에 실패했습니다."
                                            }
                                            """),
                                    @ExampleObject("""
                                            {
                                            "typeName": "API_ERROR",
                                            "message": "카카오 API호출에 실패했습니다."
                                            }
                                            """)}
                    )),

                    @ApiResponse(responseCode = "503", description = "카카오 서버에 연결할 수 없습니다.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class),
                                    examples = @ExampleObject("""
                                            {
                                            "typeName": "NETWORK_ERROR",
                                            "message": "카카오 서버에 연결할 수 없습니다."
                                            }
                                            """)))
})

@PostMapping("/login/{provider}")
public ResponseEntity<LoginResponse> login(@PathVariable String provider, @RequestBody Object request, HttpServletResponse response) {
    LoginResponse loginResponse = authService.login(provider, request);
    String message = "반갑습니다 " + loginResponse.getName() + "님";
    loginResponse.setMessage(message);

    Cookie refreshTokenCookie = new Cookie("refresh_token", jwtUtils.generateRefreshToken(loginResponse.getEmail()));
    refreshTokenCookie.setHttpOnly(true); //JavaScript에서 접근 불가능하게 설정
    refreshTokenCookie.setSecure(true); //HTTPS에서만 전송되도록 설정
    refreshTokenCookie.setPath("/"); //모든 경로에서 쿠키 사용
    refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); //7일을 초로 변환
    refreshTokenCookie.setAttribute("SameSite", "none");

    response.addCookie(refreshTokenCookie);

    //return ResponseEntity.ok(loginResponse);
    //두 방식은 동일한 결과임
    return ResponseEntity.status(HttpStatus.OK)
            .body(loginResponse);
}

@Operation(summary = "액세스 토큰 재발급")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "access_token이 재발급 되었습니다.",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = TokenResponse.class),
                        examples = @ExampleObject("{\n" +
                                "\"access_token\": \"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QG5hdmVyLmNvbSIsIm5hbWUiOiLthYzsiqTtirgiLCJuaWNrTmFtZSI6Iu2FjOyKpO2KuOuLieuEpOyehCIsImV4cCI6MTczOTEwODQ4MH0.ZaMBUEGyEf0vs2ebstGvIiBH0AhE2eWLBacn7Ex1TRA\",\n" +
                                "\"message\": \"access_token이 재발급 되었습니다.\"\n" +
                                "}"))),

        @ApiResponse(responseCode = "401", description =
                "refresh_token을 찾지 못했습니다. / refresh_token 검증에 실패했습니다. / 잘못된 서명입니다. / 만료된 인증입니다. / 지원하지 않는 인증입니다. / 잘못된 인증입니다.",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = {
                                @ExampleObject(name = "refresh_token이 없음", value = "{\n" +
                                        "\"typeName\": \"AUTH_ERROR\",\n" +
                                        "\"message\": \"refresh_token을 찾지 못했습니다.\"\n" +
                                        "}"),
                                @ExampleObject(name = "refresh_token은 있지만 기타의 사유로 검증에 실패", value = "{\n" +
                                        "\"typeName\": \"AUTH_ERROR\",\n" +
                                        "\"message\": \"refresh_token 검증에 실패했습니다.\"\n" +
                                        "}"),
                                @ExampleObject(name = "잘못된 서명일 경우", value = """
                                        {
                                        "typeName": "AUTH_ERROR",
                                        "message": "잘못된 서명입니다."
                                        }
                                        """),
                                @ExampleObject(name = "인증이 만료되었을 경우", value = """
                                        {
                                        "typeName": "AUTH_ERROR",
                                        "message": "만료된 인증입니다."
                                        }
                                        """),
                                @ExampleObject(name = "지원하지 않는 인증 형식일 경우", value = """
                                        {
                                        "typeName": "AUTH_ERROR",
                                        "message": "지원하지 않는 인증입니다."
                                        }
                                        """),
                                @ExampleObject(name = "부적절한 인자가 전달되었을 경우", value = """
                                        {
                                        "typeName": "AUTH_ERROR",
                                        "message": "잘못된 인증입니다."
                                        }
                                        """)
                        }
                ))
})
@PostMapping("/refresh")
public ResponseEntity<TokenResponse> refresh(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies == null) {
        throw new AuthException("refresh_token을 찾지 못했습니다.");
    }
    //refreshToken을 Cookie에서 찾는 2가지 방식
    //1방식
    String refresh_token = Arrays.stream(cookies)
            .filter(cookie -> cookie.getName().equals("refresh_token"))
            .findFirst()
            .map(Cookie::getValue)
            .orElseThrow(() -> new AuthException("refresh_token을 찾지 못했습니다."));
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

    if (jwtUtils.validateToken(refresh_token)) {
        String email = jwtUtils.getUserEmailFromToken(refresh_token);
        User user = userService.findByEmail(email);
        String access_token = jwtUtils.generateAccessToken(email, user.getName(), user.getNickname());

        return ResponseEntity.ok(TokenResponse.builder().access_token(access_token)
                .message("access_token이 재발급 되었습니다.").build());
    }
    throw new AuthException("refresh_token 검증에 실패했습니다.");
}

@Operation(summary = "access_token이 유효한지 확인")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유효한 토큰입니다.",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = TokenValidationResponse.class),
                        examples = @ExampleObject("{\n" +
                                "\"message\": \"유효한 토큰입니다.\"\n" +
                                "}"))),

        @ApiResponse(responseCode = "401", description =
                "잘못된 서명입니다. / 만료된 인증입니다. / 지원하지 않는 인증입니다. / 잘못된 인증입니다. / 검증에 실패했습니다.",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = {
                                @ExampleObject(name = "잘못된 서명일 경우", value = """
                                        {
                                        "typeName": "AUTH_ERROR",
                                        "message": "잘못된 서명입니다."
                                        }
                                        """),
                                @ExampleObject(name = "인증이 만료되었을 경우", value = """
                                        {
                                        "typeName": "AUTH_ERROR",
                                        "message": "만료된 인증입니다."
                                        }
                                        """),
                                @ExampleObject(name = "지원하지 않는 인증 형식일 경우", value = """
                                        {
                                        "typeName": "AUTH_ERROR",
                                        "message": "지원하지 않는 인증입니다."
                                        }
                                        """),
                                @ExampleObject(name = "부적절한 인자가 전달되었을 경우", value = """
                                        {
                                        "typeName": "AUTH_ERROR",
                                        "message": "잘못된 인증입니다."
                                        }
                                        """),
                                @ExampleObject(name = "기타의 사유로 검증에 실패한 경우", value = """
                                        {
                                        "typeName": "AUTH_ERROR",
                                        "message": "검증에 실패했습니다."
                                        }
                                        """)
                        }))
})
@PostMapping("/validate")
public ResponseEntity<TokenValidationResponse> validation_access_token(@RequestBody AccessTokenValidationRequest request) {
    if (jwtUtils.validateToken(request.getAccess_token())) {
        return ResponseEntity.ok(TokenValidationResponse.builder().message("유효한 토큰입니다.").build());
    }
    throw new AuthException("검증에 실패했습니다.");
}

@Operation(summary = "유저 삭제", security = {@SecurityRequirement(name = "JWT")})
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "사용자의 모든 정보가 삭제되었습니다.",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = DeleteUserResponse.class),
                        examples = @ExampleObject("""
                                {
                                "message": "사용자의 모든 정보가 삭제되었습니다."
                                }
                                """))),

        @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자 입니다.",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject("""
                                {
                                "typeName": "NOT_FOUND_ERROR",
                                "message": "존재하지 않는 사용자 입니다."
                                }
                                """))),

        @ApiResponse(responseCode = "500", description = "사용자 삭제에 실패했습니다.",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ErrorResponse.class),
                        examples = @ExampleObject(value = "{\n\"typeName\": \"DATABASE_ERROR\",\n\"message\": \"사용자 삭제에 실패했습니다.\"\n}")))
})
@DeleteMapping("/delete")
public ResponseEntity<DeleteUserResponse> deleteUser(@AuthenticationPrincipal String email) {
    authService.deleteUser(email);
    DeleteUserResponse response = new DeleteUserResponse();
    response.setMessage("사용자의 모든 정보가 삭제되었습니다.");

    return ResponseEntity.ok(response);
}
}
