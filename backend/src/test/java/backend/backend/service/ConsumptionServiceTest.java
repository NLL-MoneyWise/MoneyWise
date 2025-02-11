package backend.backend.service;

import backend.backend.dto.consumption.request.ConsumptionsSaveRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ConsumptionServiceTest {
    @Autowired
    private ConsumptionService consumptionService;

    @Test
    @DisplayName("ConsumptionService성공")
    void ConsumptionSaveSuccess() {
        ConsumptionsSaveRequest request = ConsumptionsSaveRequest.builder()
                .receiptId(1019L)
                .date("2015/12/19")
                .items(List.of(ConsumptionsSaveRequest.Item.builder().name("말보로레드").amount(4500L).category("잡화").build()))
                .build();

        Assertions.assertThatCode(() -> {
            consumptionService.save("test@naver.com", request);
        }).doesNotThrowAnyException();
    }
}