package backend.backend.dto.consumption.request;

import backend.backend.dto.common.model.Item;
import backend.backend.dto.receipt.response.ReceiptAnalyzeResponse;
import lombok.*;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ConsumptionsSaveRequest {
    private String access_url;
    private String date;
    private String storeName;
    private List<Item> items;

    public static ConsumptionsSaveRequest fromReceiptAnalyzeResponse(ReceiptAnalyzeResponse receiptAnalyzeResponse) {
        return ConsumptionsSaveRequest.builder()
                .date(receiptAnalyzeResponse.getDate())
                .items(receiptAnalyzeResponse.getItems())
                .storeName(receiptAnalyzeResponse.getStoreName())
                .build();
    }
}
