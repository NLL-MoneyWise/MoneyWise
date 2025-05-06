package backend.backend.dto.facade.response;

import backend.backend.dto.common.model.Item;
import backend.backend.dto.consumption.model.ConsumptionDTO;
import backend.backend.dto.consumption.response.ConsumptionsSaveResponse;
import backend.backend.dto.receipt.response.ReceiptAnalyzeResponse;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacadeReceiptProcessResponse {
    private String date;
    private Long totalAmount;
    private String store_name;
    private List<ConsumptionDTO> consumptionDTOList;
    private String message;

    public static FacadeReceiptProcessResponse fromReceiptAnalyzeAndConsumptionSaveResponse(ReceiptAnalyzeResponse receiptAnalyzeResponse, ConsumptionsSaveResponse consumptionsSaveResponse) {
        return FacadeReceiptProcessResponse.builder()
                .date(receiptAnalyzeResponse.getDate())
                .consumptionDTOList(consumptionsSaveResponse.getConsumptionDTOList())
                .totalAmount(receiptAnalyzeResponse.getTotalAmount())
                .store_name(receiptAnalyzeResponse.getStoreName())
                .build();
    }
}
