package backend.backend.service;

import backend.backend.dto.common.model.Item;
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
                .access_url("receipt.jpeg")
                .date("2015-12-19")
                .storeName("GS25")
                .items(List.of(Item.builder().name("말보로레드").amount(4500L).category("기타").quantity(1L).build()))
                .build();

        Assertions.assertThatCode(() -> {
            consumptionService.save("test@naver.com", request);
        }).doesNotThrowAnyException();
    }
}