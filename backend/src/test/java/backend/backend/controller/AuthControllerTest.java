package backend.backend.controller;

import backend.backend.dto.request.LoginRequest;
import backend.backend.security.config.SecurityConfig;
import backend.backend.security.jwt.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;  // status, jsonPath, cookie를 한번에 import

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc; //HTTP요청을 테스트하기 위한 객체임

    @MockitoBean
    private JwtUtils jwtUtils;

    @Test
    void loginSuccess() throws Exception {
        // given
        LoginRequest request = new LoginRequest();
        request.setEmail("test@naver.com");
        request.setPassword("password");

        String accessToken = "new.access.token";
        String refreshToken = "test.refresh.token";
        when(jwtUtils.generateAccessToken("test@naver.com")).thenReturn(accessToken);
        when(jwtUtils.generateRefreshToken("test@naver.com")).thenReturn(refreshToken);

        // when & then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(accessToken))
                .andExpect(jsonPath("$.userInfo.email").value("test@naver.com"))
                .andExpect(cookie().exists("refreshToken"));
    }

    @Test
    @DisplayName("리프레시 토큰으로 새로운 액세스 토큰을 발급받는다")
    void refreshTokenSuccess() throws Exception {
        // given
        String validRefreshToken = "valid.refresh.token";
        String email = "test@naver.com";
        String newAccessToken = "new.access.token";

        // JwtUtils 동작 정의
        when(jwtUtils.validateToken(validRefreshToken)).thenReturn(true);
        when(jwtUtils.getUserEmailFromToken(validRefreshToken)).thenReturn(email);
        when(jwtUtils.generateAccessToken(email)).thenReturn(newAccessToken);

        // Cookie 설정
        Cookie refreshTokenCookie = new Cookie("refrechToken", validRefreshToken);

        // when & then
        mockMvc.perform(post("/api/auth/refresh")
                        .cookie(refreshTokenCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.accessToken").value(newAccessToken))
                .andDo(print());
    }

}