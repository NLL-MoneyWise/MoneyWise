package backend.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReceiptAnalyzeResponse {
    private long receiptId;
    private String date;
    private long totalAmount;
    private Items items;

    @Getter
    @Builder
    public static class Items {
        private String category;
        private String name;
        private long amount;
    }
}
