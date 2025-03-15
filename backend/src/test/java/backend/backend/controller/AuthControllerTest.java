package backend.backend.controller;

import backend.backend.dto.auth.request.LocalLoginRequest;
import backend.backend.dto.auth.request.LocalSignupRequest;
import backend.backend.security.jwt.JwtUtils;
import backend.backend.service.LocalAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;  // status, jsonPath, cookie를 한번에 import

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc; //HTTP요청을 테스트하기 위한 객체임
    @MockBean
    private LocalAuthService localAuthService;
    @MockBean
    private JwtUtils jwtUtils;

    @Test
    void loginSuccess() throws Exception {
        // given
        String email = "test@naver.com";
        String name = "테스트";
        String nickName = "테스트닉네임";

        LocalLoginRequest request = new LocalLoginRequest();
        request.setEmail(email);
        request.setPassword("testPassword@1231");

        String accessToken = "new.access.token";
        String refreshToken = "test.refresh.token";

        when(localAuthService.login(ArgumentMatchers.any(LocalLoginRequest.class))).thenReturn(accessToken);//any가 없으면 객체 동등성이 성립되지 않아 null을 반환하는 에러 발생
        System.out.println("Generated access token: " + localAuthService.login(request));
        when(jwtUtils.generateRefreshToken(email)).thenReturn(refreshToken);
        when(jwtUtils.getUserNameFromToken(accessToken)).thenReturn(name);
        System.out.println(jwtUtils.getUserNameFromToken(accessToken));

        // when & then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                        .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(accessToken))
                .andExpect(cookie().exists("refreshToken"));
    }

    @Test
    @DisplayName("리프레시 토큰으로 새로운 액세스 토큰을 발급받는다")
    void refreshTokenSuccess() throws Exception {
        // given
        String validRefreshToken = "valid.refresh.token";
        String email = "test@naver.com";
        String name = "테스트";
        String nickName = "테스트닉네임";
        String newAccessToken = "new.access.token";

        // JwtUtils 동작 정의
        when(jwtUtils.validateToken(validRefreshToken)).thenReturn(true);
        when(jwtUtils.getUserEmailFromToken(validRefreshToken)).thenReturn(email);
        when(jwtUtils.generateAccessToken(email, name, nickName)).thenReturn(newAccessToken);

        // Cookie 설정
        Cookie refreshTokenCookie = new Cookie("refrechToken", validRefreshToken);

        // when & then
        mockMvc.perform(post("/api/auth/refresh")
                        .cookie(refreshTokenCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(newAccessToken))
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입")
    void signupSuccess() throws Exception {
        LocalSignupRequest signupRequest = new LocalSignupRequest();
        signupRequest.setName("테스트");
        signupRequest.setNickname("테스트닉네임");
        signupRequest.setPassword("testPassword@1231");
        signupRequest.setEmail("test@naver.com");
        System.out.println("sign: " + signupRequest);

        Mockito.doNothing().when(localAuthService).signup(signupRequest);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(signupRequest)))
                        .andExpect(status().isOk())
                        .andDo(print());

//        MvcResult result = mockMvc.perform(post("/api/auth/signup")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(new ObjectMapper().writeValueAsString(signupRequest)))
//                .andExpect(status().is4xxClientError())
//                .andDo(print())
//                .andReturn();
//
//        String responseBody = result.getResponse().getContentAsString();
//        System.out.println(responseBody);
    }

}