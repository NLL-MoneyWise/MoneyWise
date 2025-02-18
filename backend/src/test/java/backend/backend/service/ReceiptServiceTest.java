package backend.backend.service;

import backend.backend.dto.receipt.request.ReceiptAnalyzeRequest;
import backend.backend.dto.receipt.response.ReceiptAnalyzeResponse;
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
    void realOpenAiApiTest() {
        String email = "test@naver.com";
        String accessUrl = "receipt.jpeg";
        ReceiptAnalyzeRequest request = ReceiptAnalyzeRequest.builder().accessUrl(accessUrl).build();
        ReceiptAnalyzeResponse receiptAnalyzeResponse = receiptService.receiptAnalyze(email, request);

        Assertions.assertNotNull(receiptAnalyzeResponse);
        Assertions.assertNotNull(receiptAnalyzeResponse.getDate());
        Assertions.assertNotNull(receiptAnalyzeResponse.getTotalAmount());
        Assertions.assertNotNull(receiptAnalyzeResponse.getItems());

        System.out.println("Date: " + receiptAnalyzeResponse.getDate());
        System.out.println("Total Amount: " + receiptAnalyzeResponse.getTotalAmount());
        System.out.println("Items: " + receiptAnalyzeResponse.getItems().get(0));
        System.out.println("Id: " + receiptAnalyzeResponse.getReceiptId());
    }
}
