package backend.backend.controller;

import backend.backend.dto.facade.request.FacadeConsumptionsAnalyzeRequest;
import backend.backend.dto.facade.request.FacadeReceiptProcessRequest;
import backend.backend.dto.facade.response.FacadeConsumptionsAnalyzeResponse;
import backend.backend.dto.facade.response.FacadeReceiptProcessResponse;
import backend.backend.exception.ValidationException;
import backend.backend.exception.response.ErrorResponse;
import backend.backend.service.FacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "통합 API", description = "영수증 분석/소비 저장, 소비 분석 통합 기능")
@RestController
@RequestMapping("/workflows")
@RequiredArgsConstructor
public class FacadeController {
    private final FacadeService facadeService;

    @Operation(summary = "영수증 분석 및 소비 저장 프로세스", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "영수증 분석 및 소비 내역 저장이 완료 되었습니다.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = FacadeReceiptProcessResponse.class),
            examples = @ExampleObject("""
                    {
                    "receiptId": "1024",
                    "date": "2015/11/19",
                    "totalAmount": "4500",
                    "items": [{"category": "잡화", "name": "말보로레드", "amount": "4500"}, {"category": "문구", "name": "컴퓨터용싸인펜", "amount": "500"}],
                    "message": "영수증 분석 및 소비 내역 저장이 완료 되었습니다."
                    }
                    """))),

            @ApiResponse(responseCode = "400", description = "아이템이 비어있습니다.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class),
            examples = {
                    @ExampleObject(name = "소비 저장 시 item이 없을 경우", value = "{\n" +
                            "\"typeName\": \"VALIDATION_ERROR\",\n" +
                            "\"message\": \"아이템이 비어있습니다.\"\n" +
                            "}"),
                    @ExampleObject(name = "영수증에 가게 이름과 상품 목록이 모두 없을 경우", value = """
                            {
                            "typeName": "VALIDATION_ERROR",
                            "message": "잘못된 영수증 이미지 입니다."
                            }
                            """)
            })),

            @ApiResponse(responseCode = "500", description = "저장 관련 데이터베이스 오류 2가지",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class),
            examples = {
                    @ExampleObject(name = "영수증 분석 중 데이터베이스 저장 오류", value = """
                            {
                            "typeName": "DATABASE_ERROR",
                            "message": "영수증 저장 중 오류가 발생했습니다."
                            }
                            """),
                    @ExampleObject(name = "소비 저장 중 데이터베이스 저장 오류", value = """
                            {
                            "typeName": "DATABASE_ERROR",
                            "message": "데이터베이스 오류가 발생했습니다."
                            }
                            """)
                    }
            )),

            @ApiResponse(responseCode = "502", description = "BAD_GATEWAY_ERROR가 발생하는 4가지 경우",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class),
            examples = {
                    @ExampleObject(name = "응답 항목이 아예 없을 때", value = """
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
    @PostMapping("/receipt-process")
    public ResponseEntity<FacadeReceiptProcessResponse> receiptProcess(@AuthenticationPrincipal String email, @RequestBody FacadeReceiptProcessRequest request) {
        FacadeReceiptProcessResponse response = facadeService.receiptProcess(email, request.getAccessUrl());
        response.setMessage("영수증 분석 및 소비 내역 저장이 완료 되었습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "period 필드를 통한 통합 소비 분석", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "period 필드에 따라 응답의 message 필드가 다릅니다.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = FacadeConsumptionsAnalyzeResponse.class),
            examples = {
                    @ExampleObject(name = "period = all 일 때", value = """
                            {
                            "totalAmount": "300000",
                            "byCategory": [{"name": "식품", "amount": "200000"}, {"name": "기타", "amount": "45000"}, {"name": "음료", "amount": "25000"}, {"name": "생활용품", "amount": "30000"}],
                            "topExpenses": [{"name": "말보로레드", "amount": "45000"}],
                            "storeExpenses": [{"name": "GS25", "amount": "100000"}, {"name": "맥도날드", "amount": "200000"}],
                            "message": "전체 기간 소비 내역 분석이 완료되었습니다."
                            }
                            """),
                    @ExampleObject(name = "period = year 일 때", value = """
                            {
                            "totalAmount": "300000",
                            "byCategory": [{"name": "식품", "amount": "200000"}, {"name": "기타", "amount": "45000"}, {"name": "음료", "amount": "25000"}, {"name": "생활용품", "amount": "30000"}],
                            "topExpenses": [{"name": "말보로레드", "amount": "45000"}],
                            "storeExpenses": [{"name": "GS25", "amount": "100000"}, {"name": "맥도날드", "amount": "200000"}],
                            "message": "2015년도의 소비 내역 분석이 완료되었습니다."
                            }
                            """),
                    @ExampleObject(name = "period = month 일 때", value = """
                            {
                            "totalAmount": "50000",
                            "byCategory": [{"name": "식품", "amount": "20000"}, {"name": "기타", "amount": "18000"}, {"name": "교통/주유", "amount": "12000"}],
                            "topExpenses": [{"name": "말보로레드", "amount": "18000"}],
                            "storeExpenses": [{"name": "GS25", "amount": "30000"}, {"name": "맥도날드", "amount": "20000"}],
                            "message": "2015년 11월 소비 내역 분석이 완료되었습니다."
                            }
                            """),
                    @ExampleObject(name = "period = day 일 때", value = """
                            {
                            "totalAmount": "5000",
                            "byCategory": [{"name": "식품", "amount": "500"}, {"name": "기타", "amount": "4500"}],
                            "topExpenses": [{"name": "말보로레드", "amount": "4500"}],
                            "storeExpenses": [{"name": "GS25", "amount": "5000"}],
                            "message": "2015년 11월 19일 소비 내역 분석이 완료되었습니다."
                            }
                            """)
                    }
            )),

            @ApiResponse(responseCode = "400", description = "period 값에 따라 발생할 수 있는 오류입니다.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class),
            examples = {
                    @ExampleObject(name = "period = year이지만 year이 비어있을 때",
                    value = """
                            {
                            "typeName": "VALIDATION_ERROR",
                            "message": "year가 비어있습니다."
                            }
                            """),

                    @ExampleObject(name = "period = month이지만 year나 month가 비어있을 때",
                            value = """
                            {
                            "typeName": "VALIDATION_ERROR",
                            "message": "year나 month가 비어있습니다."
                            }
                            """),

                    @ExampleObject(name = "period = day이지만 year나 month나 day가 비어있을 때",
                            value = """
                            {
                            "typeName": "VALIDATION_ERROR",
                            "message": "year나 month나 day가 비어있습니다."
                            }
                            """)
            }))
    })
    @GetMapping("/consumptions-analyze")
        public ResponseEntity<FacadeConsumptionsAnalyzeResponse> consumptionsAnalyzeProcess(
                @AuthenticationPrincipal String email,
                @RequestParam(name = "period") String period,
                @RequestParam(required = false, name = "year") Long year,
                @RequestParam(required = false, name = "month") Long month,
                @RequestParam(required = false, name = "day") Long day) throws BadRequestException {

        if (period.equals("year") && year == null) {
            throw new ValidationException("year가 비어있습니다.");
        } else if (period.equals("month") && (year == null || month == null)) {
            throw new ValidationException("year나 month가 비어있습니다.");
        } else if (period.equals("day") && (year == null || month == null || day == null)) {
            throw new ValidationException("year나 month나 day가 비어있습니다.");
        }

        FacadeConsumptionsAnalyzeResponse response = facadeService.consumptionsAnalyzeProcess(email, year, month, day);

        if (period.equals("all")) {
            response.setMessage("전체 기간 소비 내역 분석이 완료되었습니다.");
        } else if (period.equals("year")) {
            response.setMessage(year + "년도의 소비 내역 분석이 완료되었습니다.");
        } else if (period.equals("month")) {
            response.setMessage(year + "년 " + month + "월 소비 내역 분석이 완료되었습니다.");
        } else {
            response.setMessage(year + "년 " + month + "월 " + day + "일 소비 내역 분석이 완료되었습니다.");
        }

        return ResponseEntity.ok(response);
    }
}
