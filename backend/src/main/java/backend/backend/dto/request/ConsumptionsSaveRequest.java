package backend.backend.dto.request;

import lombok.*;

import java.util.List;

@Builder
@Getter
public class ConsumptionsSaveRequest {
    private Long receiptId;
    private String date;
    private List<Item> items;

    @Getter
    @Builder
    public static class Item {
        private String category;
        private String name;
        private Long amount;
    }
}
