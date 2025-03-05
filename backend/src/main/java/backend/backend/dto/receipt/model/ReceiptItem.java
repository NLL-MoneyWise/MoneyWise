package backend.backend.dto.receipt.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptItem {
    private String category;
    private String name;
    private Long amount;
}
