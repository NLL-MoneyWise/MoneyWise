package backend.backend.controller;

import backend.backend.dto.response.ReceiptAnalyzeResponse;
import backend.backend.security.jwt.JwtUtils;
import backend.backend.service.ReceiptService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class ReceiptControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ReceiptController receiptController;

    @MockitoBean
    private ReceiptService receiptService;

    @Test
    @DisplayName("ReceiptControllerTest")
    void receiptControllerTest() throws Exception {
        String email = "test@naver.com";
        String accessUrl = "receipt.jpeg";
        String accessToken = "Bearer " + jwtUtils.generateAccessToken(email);

        ReceiptAnalyzeResponse mockResponse = new ReceiptAnalyzeResponse();
        when(receiptService.receiptAnalyze(email, accessUrl)).thenReturn(mockResponse);

        mockMvc.perform(post("/api/receipts/analyze")
                .header("Authorization", accessToken)
                .param("accessUrl", accessUrl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
