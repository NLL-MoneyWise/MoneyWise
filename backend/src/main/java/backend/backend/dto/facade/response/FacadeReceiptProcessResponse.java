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
    private Long totalAmount;
    private List<Item> items;
    private String message;

    public static FacadeReceiptProcessResponse fromReceiptAnalyzeResponse(ReceiptAnalyzeResponse receiptAnalyzeResponse) {
        return FacadeReceiptProcessResponse.builder()
                .date(receiptAnalyzeResponse.getDate())
                .items(receiptAnalyzeResponse.getItems())
                .totalAmount(receiptAnalyzeResponse.getTotalAmount())
                .build();
    }
}
