package backend.backend.service;

import backend.backend.dto.auth.request.ReceiptAnalyzeRequest;
import backend.backend.dto.consumption.request.ConsumptionsSaveRequest;
import backend.backend.dto.consumption.response.ConsumptionsSaveResponse;
import backend.backend.dto.facade.request.FacadeConsumptionsAnalyzeRequest;
import backend.backend.dto.facade.response.FacadeConsumptionsAnalyzeResponse;
import backend.backend.dto.facade.response.FacadeReceiptProcessResponse;
import backend.backend.dto.receipt.response.ReceiptAnalyzeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FacadeService {
    private final ReceiptService receiptService;
    private final ConsumptionService consumptionService;
    public FacadeReceiptProcessResponse receiptProcess(String email, String accessUrl) {
        ReceiptAnalyzeResponse receiptAnalyzeResponse = receiptService.receiptAnalyze(email, accessUrl);

        ConsumptionsSaveRequest consumptionsSaveRequest = ConsumptionsSaveRequest.fromReceiptAnalyzeResponse(receiptAnalyzeResponse);
        consumptionsSaveRequest.setAccess_url(accessUrl);

        ConsumptionsSaveResponse consumptionsSaveResponse = consumptionService.save(email, consumptionsSaveRequest);

        return FacadeReceiptProcessResponse.fromReceiptAnalyzeAndConsumptionSaveResponse(receiptAnalyzeResponse, consumptionsSaveResponse);
    }

    public FacadeConsumptionsAnalyzeResponse consumptionsAnalyzeProcess(String email, Long year, Long month, Long day) {
        FacadeConsumptionsAnalyzeResponse response = new FacadeConsumptionsAnalyzeResponse();

        response.setByCategory(consumptionService.getTotalAmountByEmailAndCategory(email, year, month, day));
        response.setTopExpenses(consumptionService.getMaxAmountByEmailAndItemName(email, year, month, day));
        response.setTotalAmount(consumptionService.getTotalAmountByEmail(email, year, month, day));
        response.setStoreExpenses(consumptionService.getStoreExpenseListByEmailAndStoreName(email, year, month, day));

        return response;
    }
}
