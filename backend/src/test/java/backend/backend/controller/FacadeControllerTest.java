package backend.backend.controller;

import backend.backend.dto.facade.request.FacadeConsumptionsAnalyzeRequest;
import backend.backend.dto.facade.request.FacadeReceiptProcessRequest;
import backend.backend.security.jwt.JwtUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class FacadeControllerTest {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    MockMvc mockMvc;
    @Test
    @DisplayName("Receipt Process성공")
    void receiptProcessTest() throws Exception {
        String email = "test@naver.com";
        String name = "테스트";
        String nickName = "테스트닉네임";
        String accessToken = "Bearer " + jwtUtils.generateAccessToken(email, name, nickName);

        FacadeReceiptProcessRequest request = FacadeReceiptProcessRequest.builder().accessUrl("receipt_mobile.jpg").build();

        mockMvc.perform(MockMvcRequestBuilders.post("/workflows/receipt-process")
                        .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("consumptionsProcessSuccess")
    void consumptionsProcessSuccess() throws Exception {
        String email = "test@naver.com";
        String name = "테스트";
        String nickName = "테스트닉네임";
        String accessToken = "Bearer " + jwtUtils.generateAccessToken(email, name, nickName);

        mockMvc.perform(MockMvcRequestBuilders.get("/workflows/consumptions-analyze")
                .header("Authorization", accessToken)
                        .param("period", "all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.get("/workflows/consumptions-analyze")
                        .header("Authorization", accessToken)
                        .param("period", "day")
                        .param("year", "2015")
                        .param("month", "12")
                        .param("day", "20"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}