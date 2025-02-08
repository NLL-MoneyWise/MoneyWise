package backend.backend.service;

import backend.backend.dto.response.ReceiptAnalyzeResponse;
import backend.backend.exception.JsonParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
public class ReceiptServiceTest {
    @Autowired
    private ReceiptService receiptService;

    @Test
    @DisplayName("실제 Open AI API호출 테스트")
    void realOpenAiApiTest() throws JsonParseException {
        String email = "test@naver.com";
        String accessUrl = "receipt.jpeg";
        ReceiptAnalyzeResponse receiptAnalyzeResponse = receiptService.receiptAnalyze(email, accessUrl);

        Assertions.assertNotNull(receiptAnalyzeResponse);
        Assertions.assertNotNull(receiptAnalyzeResponse.getDate());
        Assertions.assertNotNull(receiptAnalyzeResponse.getTotalAmount());
        Assertions.assertNotNull(receiptAnalyzeResponse.getItems());

        System.out.println("Date: " + receiptAnalyzeResponse.getDate());
        System.out.println("Total Amount: " + receiptAnalyzeResponse.getTotalAmount());
        System.out.println("Items: " + receiptAnalyzeResponse.getItems());
        System.out.println("Id: " + receiptAnalyzeResponse.getReceiptId());
    }
}
