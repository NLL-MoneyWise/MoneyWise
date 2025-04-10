package backend.backend.controller;

import backend.backend.dto.receipt.request.ReceiptAnalyzeRequest;
import backend.backend.dto.receipt.response.ReceiptAnalyzeResponse;
import backend.backend.security.jwt.JwtUtils;
import backend.backend.service.ReceiptService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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

//    @MockitoBean
    @Autowired
    private ReceiptService receiptService;

    @Test
    @DisplayName("ReceiptAnalyze성공")
    void receiptControllerTest() throws Exception {
        String email = "test@naver.com";
        String name = "테스트";
        String nickName = "테스트닉네임";
        String accessUrl = "receipt.jpeg";
        String accessToken = "Bearer " + jwtUtils.generateAccessToken(email, name, nickName);

        ReceiptAnalyzeRequest request = ReceiptAnalyzeRequest.builder().accessUrl(accessUrl).build();
        ReceiptAnalyzeResponse mockResponse = ReceiptAnalyzeResponse.builder().build();
        when(receiptService.receiptAnalyze(ArgumentMatchers.any(String.class), ArgumentMatchers.any(String.class))).thenReturn(mockResponse);
//ArgumentMatchers.eq(email) : receiptService.receiptAnalyze를 컨트롤러에서 호출 할 때 email의 값을 확인함 객체가 동등한지는 확인안함
//Json 파싱에는 기본생성자 및 setter가 필요하다.
        mockMvc.perform(post("/api/receipts/analyze")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("모든 영수증 사진 불러오기")
    void imagesSuccess() throws Exception {
        String email = "test@naver.com";
        String name = "테스트";
        String nickName = "테스트닉네임";
        String accessToken = "Bearer " + jwtUtils.generateAccessToken(email, name, nickName);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/receipts/images")
                .header("Authorization", accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
