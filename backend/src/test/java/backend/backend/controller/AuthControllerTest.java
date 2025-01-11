package backend.backend.controller;

import backend.backend.dto.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;  // status, jsonPath, cookie를 한번에 import

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc; //HTTP요청을 테스트하기 위한 객체임

    @Test
    void loginSuccess() throws Exception {
        // given
        LoginRequest request = new LoginRequest();
        request.setEmail("test@naver.com");
        request.setPassword("password");

        // when
        ResultActions result = mockMvc.perform(post("/api/auth/login") //post요청 생성
                .contentType(MediaType.APPLICATION_JSON)    //JSON타입 지정, 헤더에 JSON타입을 명시함
                .content(new ObjectMapper().writeValueAsString(request))); //객체를 JSON문자열로 변환

        // then
        result.andExpect(status().isOk()) //상태코드 검증
                .andExpect(jsonPath("$.accessToken").exists()) //andExpect = 검증메서드, exists=필드 존재 여부
                .andExpect(jsonPath("$.userInfo.email").value("test@naver.com")) //value = 값 검증
                .andExpect(cookie().exists("refreshToken"))
                .andDo(print()); //결과 출력
    }
}