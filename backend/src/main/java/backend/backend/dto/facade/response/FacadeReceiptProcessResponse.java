package backend.backend.dto.facade.response;

import backend.backend.dto.common.model.Item;
import backend.backend.dto.receipt.response.ReceiptAnalyzeResponse;
import lombok.*;

import java.util.Collections;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacadeReceiptProcessResponse {
    private String date;
    private Long total_amount;
    private List<Item> items;
    private String message;
    private String store_name;

    public static FacadeReceiptProcessResponse fromReceiptAnalyzeResponse(ReceiptAnalyzeResponse receiptAnalyzeResponse) {
        return FacadeReceiptProcessResponse.builder()
                .date(receiptAnalyzeResponse.getDate())
                .items(receiptAnalyzeResponse.getItems())
                .total_amount(receiptAnalyzeResponse.getTotalAmount())
                .store_name(receiptAnalyzeResponse.getStoreName())
                .build();
    }
}
