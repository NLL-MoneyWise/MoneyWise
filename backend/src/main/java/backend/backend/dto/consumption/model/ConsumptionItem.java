package backend.backend.dto.consumption.model;

import backend.backend.dto.receipt.model.ReceiptItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionItem {
    private String category;
    private String name;
    private Long amount;

    public static ConsumptionItem fromReceiptItem(ReceiptItem item) {
        return ConsumptionItem.builder()
                .category(item.getCategory())
                .name(item.getName())
                .amount(item.getAmount())
                .build();
    }
}
