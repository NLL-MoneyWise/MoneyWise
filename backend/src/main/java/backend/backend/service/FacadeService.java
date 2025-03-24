package backend.backend.service;

import backend.backend.dto.auth.request.ReceiptAnalyzeRequest;
import backend.backend.dto.consumption.request.ConsumptionsSaveRequest;
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

        consumptionService.save(email, consumptionsSaveRequest);

        return FacadeReceiptProcessResponse.fromReceiptAnalyzeResponse(receiptAnalyzeResponse);
    }

    public FacadeConsumptionsAnalyzeResponse consumptionsAnalyzeProcess(FacadeConsumptionsAnalyzeRequest request, String email) {
        FacadeConsumptionsAnalyzeResponse response = new FacadeConsumptionsAnalyzeResponse();
        if (request.getPeriod().equals("all")) {

            response.setByCategory(consumptionService.getTotalAmountByEmailAndCategory(email, null, null, null));
            response.setTopExpenses(consumptionService.getMaxAmountByEmailAndItemName(email, null, null, null));
            response.setTotalAmount(consumptionService.getTotalAmountByEmail(email, null, null, null));
            response.setMessage("전체 기간 소비 내역 분석이 완료되었습니다.");

        } else if (request.getPeriod().equals("year")) {

            response.setByCategory(consumptionService.getTotalAmountByEmailAndCategory(email, request.getYear(), null, null));
            response.setTopExpenses(consumptionService.getMaxAmountByEmailAndItemName(email, request.getYear(), null, null));
            response.setTotalAmount(consumptionService.getTotalAmountByEmail(email, request.getYear(), null, null));
            response.setMessage(request.getYear() + "년의 소비 내역 분석이 완료되었습니다.");

        } else {

            response.setByCategory(consumptionService.getTotalAmountByEmailAndCategory(email, request.getYear(), request.getMonth(), null));
            response.setTopExpenses(consumptionService.getMaxAmountByEmailAndItemName(email, request.getYear(), request.getMonth(), null));
            response.setTotalAmount(consumptionService.getTotalAmountByEmail(email, request.getYear(), request.getMonth(), null));
            response.setMessage(request.getYear() + "년 " + request.getMonth() + "월 소비 내역 분석이 완료되었습니다.");

        }

        return response;
    }
}
