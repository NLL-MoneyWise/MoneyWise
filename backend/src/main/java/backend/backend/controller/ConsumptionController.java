package backend.backend.controller;

import backend.backend.dto.consumption.request.ConsumptionsSaveRequest;
import backend.backend.dto.consumption.response.ConsumptionsSaveResponse;
import backend.backend.dto.consumption.response.ConsumptionsSummaryResponse;
import backend.backend.dto.consumption.response.ConsumptionsYearMonthResponse;
import backend.backend.dto.consumption.response.ConsumptionsYearResponse;
import backend.backend.exception.response.ErrorResponse;
import backend.backend.service.ConsumptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Consumptions")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/consumptions")
public class ConsumptionController {
    private final ConsumptionService consumptionService;

    @Operation(summary = "소비 저장 기능", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소비내역 저장이 완료되었습니다.",
            content = @Content(mediaType = "apllication/json",
            schema = @Schema(implementation = ConsumptionsSaveResponse.class),
            examples = @ExampleObject("{\n" +
                    "\"message\": \"소비내역 저장이 완료되었습니다.\",\n" +
                    "}"))),

            @ApiResponse(responseCode = "400", description = "아이템이 비어있습니다.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class),
            examples = @ExampleObject("{\n" +
                    "\"typeName\": \"VALIDATION_ERROR\",\n" +
                    "\"message\": \"아이템이 비어있습니다.\"\n" +
                    "}"))),

            @ApiResponse(responseCode = "500", description = "데이터베이스 오류가 발생했습니다.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class),
            examples = @ExampleObject("{\n" +
                    "\"typeName\": \"DATABASE_ERROR\",\n" +
                    "\"message\": \"데이터베이스 오류가 발생했습니다.\"\n" +
                    "}")))

    })
    @PostMapping("/save")
    public ResponseEntity<ConsumptionsSaveResponse> consumptionSave(@AuthenticationPrincipal String email
            ,@RequestBody ConsumptionsSaveRequest request) {
        consumptionService.save(email, request);
        ConsumptionsSaveResponse consumptionsSaveResponse = ConsumptionsSaveResponse.builder()
                .message("소비내역 저장이 완료되었습니다.").build();
        return ResponseEntity.ok(consumptionsSaveResponse);
    }

    @Operation(summary = "전체 기간 소비 분석", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 기간 소비 내역 분석이 완료되었습니다.",
                    content = @Content(mediaType = "apllication/json",
                            schema = @Schema(implementation = ConsumptionsSummaryResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "\"totalAmount\": \"36000\",\n" +
                                    "\"byCategory\": [{\"name\": \"잡화\",\"amount\": \"36000\"}],\n" +
                                    "\"topExpenses\": [{\"name\": \"말보로레드\",\"amount\": \"22500\"}],\n" +
                                    "\"message\":\"전체 기간 소비 내역 분석이 완료되었습니다.\"\n" +
                                    "}")))
    })
    @GetMapping("/summary")
    public ResponseEntity<ConsumptionsSummaryResponse> consumptionSummary(@AuthenticationPrincipal String email) {
        ConsumptionsSummaryResponse response = ConsumptionsSummaryResponse.builder()
                .byCategory(consumptionService.getTotalAmountByEmailAndCategory(email))
                .totalAmount(consumptionService.getTotalAmountByEmail(email))
                .topExpenses(consumptionService.getMaxAmountByEmailAndItemName(email))
                .message("전체 기간 소비 내역 분석이 완료되었습니다.")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "특정 년도 기간 소비 분석", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "{year}년의 소비 내역 분석이 완료되었습니다.",
                    content = @Content(mediaType = "apllication/json",
                            schema = @Schema(implementation = ConsumptionsYearResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "\"totalAmount\": \"36000\",\n" +
                                    "\"byCategory\": [{\"name\": \"잡화\",\"amount\": \"36000\"}],\n" +
                                    "\"topExpenses\": [{\"name\": \"말보로레드\",\"amount\": \"22500\"}],\n" +
                                    "\"message\":\"2015년의 소비 내역 분석이 완료되었습니다.\"\n" +
                                    "}")))
    })
    @GetMapping("/{year}")
    public ResponseEntity<ConsumptionsYearResponse> consumptionYear(@AuthenticationPrincipal String email, @PathVariable("year") Long year) {
        ConsumptionsYearResponse response = ConsumptionsYearResponse.builder()
                .byCategory(consumptionService.getTotalAmountByEmailAndCategoryAndYear(email, year))
                .topExpenses(consumptionService.getMaxAmountByEmailAndItemNameAndYear(email, year))
                .totalAmount(consumptionService.getTotalAmountByEmailAndYear(email, year))
                .message(year + "년도의 소비 내역 분석이 완료되었습니다.")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "특정 년월 기간 소비 분석", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "{year}년 {month}월 소비 내역 분석이 완료되었습니다.",
                    content = @Content(mediaType = "apllication/json",
                            schema = @Schema(implementation = ConsumptionsYearMonthResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "\"totalAmount\": \"36000\",\n" +
                                    "\"byCategory\": [{\"name\": \"잡화\",\"amount\": \"36000\"}],\n" +
                                    "\"topExpenses\": [{\"name\": \"말보로레드\",\"amount\": \"22500\"}],\n" +
                                    "\"message\":\"2015년 11월 소비 내역 분석이 완료되었습니다.\"\n" +
                                    "}")))
    })
    @GetMapping("/{year}/{month}")
    public ResponseEntity<ConsumptionsYearMonthResponse> consumptionYearMonth(@AuthenticationPrincipal String email, @PathVariable("year") Long year, @PathVariable("month") Long month) {
        ConsumptionsYearMonthResponse response = ConsumptionsYearMonthResponse.builder()
                .byCategory(consumptionService.getTotalAmountByEmailAndCategoryAndYearAndMonth(email, year, month))
                .topExpenses(consumptionService.getMaxAmountByEmailAndItemNameAndYearAndMonth(email, year, month))
                .totalAmount(consumptionService.getTotalAmountByEmailAndYearAndMonth(email, year, month))
                .message(year + "년 " + month + "월 소비 내역 분석이 완료되었습니다.")
                .build();

        return ResponseEntity.ok(response);
    }
}
