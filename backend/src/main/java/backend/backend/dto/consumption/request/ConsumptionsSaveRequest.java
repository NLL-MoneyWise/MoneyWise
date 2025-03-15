package backend.backend.dto.consumption.request;

import backend.backend.dto.consumption.model.ConsumptionItem;
import backend.backend.dto.receipt.model.ReceiptItem;
import backend.backend.dto.receipt.response.ReceiptAnalyzeResponse;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionsSaveRequest {
    private Long receiptId;
    private String date;
    private List<ConsumptionItem> items;

    public static ConsumptionsSaveRequest fromReceiptAnalyzeResponse(ReceiptAnalyzeResponse receiptAnalyzeResponse) {
        // Item 객체 변환
        List<ConsumptionItem> consumptionItems = new ArrayList<>();

        for(ReceiptItem item : receiptAnalyzeResponse.getItems()) {
            ConsumptionItem consumptionItem = ConsumptionItem.fromReceiptItem(item);
            consumptionItems.add(consumptionItem);
        }

        return ConsumptionsSaveRequest.builder()
                .receiptId(receiptAnalyzeResponse.getReceiptId())
                .date(receiptAnalyzeResponse.getDate())
                .items(consumptionItems)
                .build();
    }
}
