package backend.backend.dto.receipt.response;

import backend.backend.dto.receipt.model.ReceiptItem;
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
    private List<ReceiptItem> items;
    private String message;
}
