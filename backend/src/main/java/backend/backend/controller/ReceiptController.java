package backend.backend.controller;

import backend.backend.dto.request.ReceiptAnalyzeRequest;
import backend.backend.dto.response.ReceiptAnalyzeResponse;
import backend.backend.exception.JsonParseException;
import backend.backend.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
public class ReceiptController {
    private final ReceiptService receiptService;
    @PostMapping("/analyze")
    public ResponseEntity<ReceiptAnalyzeResponse> analyze(@AuthenticationPrincipal String email, @RequestBody ReceiptAnalyzeRequest request) {
        return ResponseEntity.ok(receiptService.receiptAnalyze(email, request.getAccessUrl()));
    }
}
