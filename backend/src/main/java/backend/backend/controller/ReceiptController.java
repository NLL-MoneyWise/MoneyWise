package backend.backend.controller;

import backend.backend.dto.receipt.request.ReceiptAnalyzeRequest;
import backend.backend.dto.receipt.response.ReceiptAnalyzeResponse;
import backend.backend.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
public class ReceiptController {
    private final ReceiptService receiptService;
    @PostMapping("/analyze")
    public ResponseEntity<ReceiptAnalyzeResponse> analyze(@AuthenticationPrincipal String email, @RequestBody ReceiptAnalyzeRequest request) {
        ReceiptAnalyzeResponse response = receiptService.receiptAnalyze(email, request);
        response.setMessage("영수증 분석이 완료되었습니다.");
        return ResponseEntity.ok(response);
    }
}
