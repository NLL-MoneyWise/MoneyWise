package backend.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
@Setter
public class ReceiptAnalyzeResponse {
    private Long receiptId;
    private String date;
    private Long totalAmount;
    private List<Item> items;

    @Getter
    @Builder
    public static class Item {
        private String category;
        private String name;
        private long amount;
    }
}
