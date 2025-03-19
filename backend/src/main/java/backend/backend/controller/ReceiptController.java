package backend.backend.controller;

import backend.backend.dto.consumption.response.ConsumptionsSaveResponse;
import backend.backend.dto.receipt.model.ReceiptUrlInfo;
import backend.backend.dto.receipt.request.ReceiptAnalyzeRequest;
import backend.backend.dto.receipt.response.GetAllReceiptPresignedUrlResponse;
import backend.backend.dto.receipt.response.ReceiptAnalyzeResponse;
import backend.backend.exception.response.ErrorResponse;
import backend.backend.service.ReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Receipt Relevant")
@RestController
@RequestMapping("/api/receipts")
@RequiredArgsConstructor
public class ReceiptController {
    private final ReceiptService receiptService;

    @Operation(summary = "영수증 분석 기능", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "영수증 분석이 완료되었습니다.",
                    content = @Content(mediaType = "apllication/json",
                            schema = @Schema(implementation = ReceiptAnalyzeResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "receiptId": "1024",
                                    "date": "2015/11/19",
                                    "totalAmount": "4500",
                                    "message": "영수증 분석이 완료되었습니다.",
                                    "items": [{"category": "잡화", "name": "말보로레드", "amount": 4500}, {"category": "문구", "name": null, "amount": "0"}, {"category": "식품", "name": null, "amount": "0"},{"category": "음료", "name": null, "amount": "0"}]
                                    }
                                    """))),

            @ApiResponse(responseCode = "500", description = "영수증 저장 중 오류가 발생했습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                    {
                    "typeName": "DATABASE_ERROR",
                    "message": "영수증 저장 중 오류가 발생했습니다."
                    }
                    """))),

            @ApiResponse(responseCode = "502", description = "BAD_GATEWAY_ERROR가 발생하는 4가지 경우",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class),
            examples = {@ExampleObject(name = "응답 항목이 아예 없을 때", value = """
                    {
                    "typeName": "BAD_GATEWAY_ERROR",
                    "message": "OpenAI 응답에 선택 항목이 없습니다."
                    }
                    """),
                    @ExampleObject(name = "응답 항목은 있지만 내용이 없을 때", value = """
                    {
                    "typeName": "BAD_GATEWAY_ERROR",
                    "message": "OpenAI 응답 메시지가 비어있습니다."
                    }
                    """),
                    @ExampleObject(name = "Open AI 응답을 Json으로 파싱하지 못했을 때", value = """
                    {
                    "typeName": "BAD_GATEWAY_ERROR",
                    "message": "Open Ai Response Json파싱 실패"
                    }
                    """),
                    @ExampleObject(name = "Open AI를 호출하지 못했을 때", value = """
                    {
                    "typeName": "BAD_GATEWAY_ERROR",
                    "message": "Open AI API 호출 실패"
                    }
                    """)
                }
            ))
    })
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
