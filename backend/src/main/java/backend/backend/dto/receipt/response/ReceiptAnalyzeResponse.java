package backend.backend.dto.receipt.response;

import backend.backend.dto.common.model.Item;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptAnalyzeResponse {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean error;
    private String storeName;
    private String date;
    private Long totalAmount;
    private List<Item> items;
    private String message;
}
