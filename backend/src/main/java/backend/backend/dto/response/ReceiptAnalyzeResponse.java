package backend.backend.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptAnalyzeResponse {
    private Long receiptId;
    private String date;
    private Long totalAmount;
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
