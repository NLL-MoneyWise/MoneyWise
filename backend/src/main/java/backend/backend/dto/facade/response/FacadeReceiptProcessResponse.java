package backend.backend.dto.facade.response;

import backend.backend.dto.consumption.model.ConsumptionItem;
import backend.backend.dto.receipt.response.ReceiptAnalyzeResponse;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacadeReceiptProcessResponse {
    private Long receiptId;
    private String date;
    private Long totalAmount;
    private List<ConsumptionItem> items;
    private String message;

    public static FacadeReceiptProcessResponse fromReceiptAnalyzeResponse(ReceiptAnalyzeResponse receiptAnalyzeResponse) {
        List<ConsumptionItem> consumptionItems = receiptAnalyzeResponse.getItems().stream().map(receiptItem -> ConsumptionItem.fromReceiptItem(receiptItem)).toList();

        return FacadeReceiptProcessResponse.builder()
                .receiptId(receiptAnalyzeResponse.getReceiptId())
                .date(receiptAnalyzeResponse.getDate())
                .items(consumptionItems)
                .totalAmount(receiptAnalyzeResponse.getTotalAmount())
                .build();
    }
}
