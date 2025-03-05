package backend.backend.controller;

import backend.backend.dto.receipt.model.ReceiptUrlInfo;
import backend.backend.dto.receipt.request.ReceiptAnalyzeRequest;
import backend.backend.dto.receipt.response.GetAllReceiptPresignedUrlResponse;
import backend.backend.dto.receipt.response.ReceiptAnalyzeResponse;
import backend.backend.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
public class ReceiptController {
    private final ReceiptService receiptService;
    @PostMapping("/analyze")
    public ResponseEntity<ReceiptAnalyzeResponse> analyze(@AuthenticationPrincipal String email, @RequestBody ReceiptAnalyzeRequest request) {
        ReceiptAnalyzeResponse response = receiptService.receiptAnalyze(email, request.getAccessUrl());
        response.setMessage("영수증 분석이 완료되었습니다.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/images")
    public ResponseEntity<GetAllReceiptPresignedUrlResponse> images(@AuthenticationPrincipal String email) {
        List<ReceiptUrlInfo> allReceiptUrlInfo = receiptService.getAllReceiptCloudFrontSignedUrl(email);

        return ResponseEntity.ok(GetAllReceiptPresignedUrlResponse.builder()
                .allReceiptUrlInfo(allReceiptUrlInfo).message("모든 이미지를 불러왔습니다.").build());
    }
}
