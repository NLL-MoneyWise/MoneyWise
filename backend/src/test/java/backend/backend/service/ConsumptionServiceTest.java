package backend.backend.service;

import backend.backend.dto.request.ConsumptionsSaveRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConsumptionServiceTest {
    @Autowired
    private ConsumptionService consumptionService;

    @Test
    @DisplayName("ConsumptionService성공")
    void ConsumptionSaveSuccess() {
        ConsumptionsSaveRequest request = ConsumptionsSaveRequest.builder()
                .receiptId(1019L)
                .date("2015/11/19")
                .items(List.of(ConsumptionsSaveRequest.Item.builder().name("말보로레드").amount(4500L).category("잡화").build()))
                .build();

        boolean TF = consumptionService.save("test@naver.com", request);

        Assertions.assertThat(TF).isTrue();
    }
}