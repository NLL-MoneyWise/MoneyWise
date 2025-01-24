package backend.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class receiptAnalyzeResponse {
    private long receiptId;
    private String date;
    private long totalAmount;
    private Items items;

    public static class Items {
        private String category;
        private String name;
        private long amount;
    }
}
