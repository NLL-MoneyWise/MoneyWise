package backend.backend.service;

import backend.backend.dto.auth.request.ReceiptAnalyzeRequest;
import backend.backend.dto.consumption.request.ConsumptionsSaveRequest;
import backend.backend.dto.facade.response.FacadeReceiptProcessResponse;
import backend.backend.dto.receipt.response.ReceiptAnalyzeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FacadeService {
    private final ReceiptService receiptService;
    private final ConsumptionService consumptionService;
    public FacadeReceiptProcessResponse receiptProcess(String email, String accessUrl) {
        ReceiptAnalyzeResponse receiptAnalyzeResponse = receiptService.receiptAnalyze(email, accessUrl);

        ConsumptionsSaveRequest consumptionsSaveRequest = ConsumptionsSaveRequest.fromReceiptAnalyzeResponse(receiptAnalyzeResponse);

        consumptionService.save(email, consumptionsSaveRequest);

        return FacadeReceiptProcessResponse.fromReceiptAnalyzeResponse(receiptAnalyzeResponse);
    }
}
