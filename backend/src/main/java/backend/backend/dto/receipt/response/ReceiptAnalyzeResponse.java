package backend.backend.dto.receipt.response;

import backend.backend.dto.receipt.model.ReceiptItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptAnalyzeResponse {
    private Long receiptId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean error;
    private String storeName;
    private String date;
    private Long totalAmount;
    private List<ReceiptItem> items;
    private String message;
}
