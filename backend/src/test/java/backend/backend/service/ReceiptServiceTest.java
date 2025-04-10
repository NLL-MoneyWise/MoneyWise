package backend.backend.service;

import backend.backend.dto.receipt.request.ReceiptAnalyzeRequest;
import backend.backend.dto.receipt.response.ReceiptAnalyzeResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReceiptServiceTest {
    @Autowired
    private ReceiptService receiptService;

    @Test
    @DisplayName("실제 Open AI API호출 테스트")
    void realOpenAiApiTest() throws JsonProcessingException {
        String email = "test@naver.com";
        String accessUrl = "receipt.jpeg";
        ReceiptAnalyzeResponse receiptAnalyzeResponse = receiptService.receiptAnalyze(email, accessUrl);

        Assertions.assertNotNull(receiptAnalyzeResponse);
        Assertions.assertNotNull(receiptAnalyzeResponse.getDate());
        Assertions.assertNotNull(receiptAnalyzeResponse.getTotalAmount());

        System.out.println("Date: " + receiptAnalyzeResponse.getDate());
        System.out.println("StoreName: " + receiptAnalyzeResponse.getStoreName());
        System.out.println("Total Amount: " + receiptAnalyzeResponse.getTotalAmount());
        System.out.println("Items: " + new ObjectMapper().writeValueAsString(receiptAnalyzeResponse.getItems()));
    }
}
