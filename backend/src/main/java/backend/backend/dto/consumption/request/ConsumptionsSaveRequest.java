package backend.backend.dto.consumption.request;

import lombok.*;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionsSaveRequest {
    private Long receiptId;
    private String date;
    private List<Item> items;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private String category;
        private String name;
        private Long amount;
    }
}
