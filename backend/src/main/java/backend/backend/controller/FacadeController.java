package backend.backend.controller;

import backend.backend.dto.facade.request.FacadeConsumptionsAnalyzeRequest;
import backend.backend.dto.facade.request.FacadeReceiptProcessRequest;
import backend.backend.dto.facade.response.FacadeConsumptionsAnalyzeResponse;
import backend.backend.dto.facade.response.FacadeReceiptProcessResponse;
import backend.backend.service.FacadeService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workflows")
@RequiredArgsConstructor
public class FacadeController {
    private final FacadeService facadeService;
    @PostMapping("/receipt-process")
    public ResponseEntity<FacadeReceiptProcessResponse> receiptProcess(@AuthenticationPrincipal String email, @RequestBody FacadeReceiptProcessRequest request) {
        FacadeReceiptProcessResponse response = facadeService.receiptProcess(email, request.getAccessUrl());
        response.setMessage("영수증 분석 및 소비 내역 저장이 완료 되었습니다.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/consumptions-analyze")
        public ResponseEntity<FacadeConsumptionsAnalyzeResponse> consumptionsAnalyzeProcess(
                @AuthenticationPrincipal String email,
                @RequestParam(name = "period") String period,
                @RequestParam(required = false, name = "year") Long year,
                @RequestParam(required = false, name = "month") Long month) throws BadRequestException {

        if (period.equals("year") && year == null) {
            throw new BadRequestException("year가 비어있습니다.");
        } else if (period.equals("month") && (year == null || month == null)) {
            throw new BadRequestException("year과 month가 비어있습니다.");
        }

        FacadeConsumptionsAnalyzeRequest request = FacadeConsumptionsAnalyzeRequest.builder()
                .period(period).year(year).month(month).build();

        FacadeConsumptionsAnalyzeResponse response = facadeService.consumptionsAnalyzeProcess(request, email);
        return ResponseEntity.ok(response);
    }
}
