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

        FacadeReceiptProcessRequest request = FacadeReceiptProcessRequest.builder().accessUrl("receipt.jpeg").build();

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

        FacadeConsumptionsAnalyzeRequest allRequest = FacadeConsumptionsAnalyzeRequest.builder()
                .period("all").build();

        mockMvc.perform(MockMvcRequestBuilders.get("/workflows/consumptions-analyze")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(allRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        FacadeConsumptionsAnalyzeRequest yearRequest = FacadeConsumptionsAnalyzeRequest.builder()
                .period("year").year(2015L).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/workflows/consumptions-analyze")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(yearRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        FacadeConsumptionsAnalyzeRequest monthRequest = FacadeConsumptionsAnalyzeRequest.builder()
                .period("month").year(2015L).month(11L).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/workflows/consumptions-analyze")
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(monthRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}