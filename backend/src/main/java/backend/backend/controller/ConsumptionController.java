package backend.backend.controller;

import backend.backend.dto.consumption.request.ConsumptionsSaveRequest;
import backend.backend.dto.consumption.request.ConsumptionsUpdateRequest;
import backend.backend.dto.consumption.response.*;
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
@RequestMapping("/consumptions")
public class ConsumptionController {
    private final ConsumptionService consumptionService;

    @Operation(summary = "소비 저장 기능", security = {@SecurityRequirement(name = "JWT")}, hidden = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소비 내역 저장이 완료되었습니다.",
                    content = @Content(mediaType = "apllication/json",
                            schema = @Schema(implementation = ConsumptionsSaveResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "consumptionDTOList": [{"id": "2000", "category": "잡화", "name": "말보로레드", "amount": "4500", "quantity": "1"}, {"id": "2001", "category": "문구", "name": "컴퓨터용싸인펜", "amount": "1000", "quantity": "2"}],
                                    "message": "소비 내역 저장이 완료되었습니다."
                                    }
                                    """))),

            @ApiResponse(responseCode = "404", description = "해당하는 영수증이 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "NOT_FOUND_ERROR",
                                    "message": "해당하는 영수증이 없습니다."
                                    }
                                    """))),

            @ApiResponse(responseCode = "500", description = "소비 저장 중 오류가 발생했습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("{\n" +
                                    "\"typeName\": \"DATABASE_ERROR\",\n" +
                                    "\"message\": \"소비 저장 중 오류가 발생했습니다.\"\n" +
                                    "}")))
    })
    @PostMapping("/save")
    public ResponseEntity<ConsumptionsSaveResponse> consumptionSave(@AuthenticationPrincipal String email
            , @RequestBody ConsumptionsSaveRequest request) {
        ConsumptionsSaveResponse response = consumptionService.save(email, request);
        response.setMessage("소비 내역 저장이 완료되었습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "소비 내역 변경", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소비 내역이 변경되었습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsumptionsUpdateResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "message": "소비 내역이 변경되었습니다."
                                    }
                                    """))),

            @ApiResponse(responseCode = "400", description = "잘못된 날짜 형식입니다. yyyy-MM-dd를 사용해주세요.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "VALIDATION_ERROR",
                                    "message": "잘못된 날짜 형식입니다. yyyy-MM-dd를 사용해주세요."
                                    }
                                    """))),

            @ApiResponse(responseCode = "404", description = "{id}번의 소비 내역이 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "NOT_FOUND_ERROR",
                                    "message": "2000번의 소비 내역이 없습니다."
                                    }
                                    """))),

            @ApiResponse(responseCode = "500", description = "소비 내역 변경에 실패했습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "DATABASE_ERROR",
                                    "message": "소비 내역 변경에 실패했습니다."
                                    }
                                    """)))
    })
    @PutMapping("/update")
    public ResponseEntity<ConsumptionsUpdateResponse> consumptionsUpdate(@AuthenticationPrincipal String email, @RequestBody ConsumptionsUpdateRequest request) {
        ConsumptionsUpdateResponse response = consumptionService.update(email, request);
        response.setMessage("소비 내역이 변경되었습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "소비 내역 조회", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "해당 영수증에 대한 모든 소비 내역을 불러왔습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsumptionsFindAllResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "consumptionDTOList": [{"id": "2000", "category": "잡화", "name": "말보로레드", "amount": "4500", "quantity": "1"}, {"id": "2001", "category": "문구", "name": "컴퓨터용싸인펜", "amount": "1000", "quantity": "2"}],
                                    "store_name": "CU",
                                    "date": "2025-05-06",
                                    "message": "해당 영수증에 대한 모든 소비 내역을 불러왔습니다."
                                    }
                                    """))),

            @ApiResponse(responseCode = "404", description = "해당 영수증의 소비 내역을 찾을 수 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "NOT_FOUND_ERROR",
                                    "message": "해당 영수증의 소비 내역을 찾을 수 없습니다."
                                    }
                                    """)))
    })
    @GetMapping("/find/all/{access_url}")
    public ResponseEntity<ConsumptionsFindAllResponse> consumptionFindAll(@AuthenticationPrincipal String email, @PathVariable String access_url) {
        ConsumptionsFindAllResponse response = consumptionService.findAll(email, access_url);

        response.setMessage("해당 영수증에 대한 모든 소비 내역을 불러왔습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "소비 내역 id로 조회(단일)", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "{id}번의 소비 내용이 조회되었습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsumptionsFindAllResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "consumptionDTO": {"id": "2000", "category": "잡화", "name": "말보로레드", "amount": "4500", "quantity": "1"}, {"id": "2001", "category": "문구", "name": "컴퓨터용싸인펜", "amount": "1000", "quantity": "2"},
                                    "store_name": "CU",
                                    "date": "2025-05-06",
                                    "message": "해당 영수증에 대한 모든 소비 내역을 불러왔습니다."
                                    }
                                    """))),

            @ApiResponse(responseCode = "404", description = "해당 id의 소비 내역이 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "NOT_FOUND_ERROR",
                                    "message": "해당 id의 소비 내역이 없습니다."
                                    }
                                    """)))
    })
    @GetMapping("/find/one/{id}")
    public ResponseEntity<ConsumptionsFindOneResponse> consumptionFindOne(@AuthenticationPrincipal String email, @PathVariable Long id) {
        ConsumptionsFindOneResponse response = consumptionService.findOne(email, id);

        response.setMessage(id + "번의 소비 내용이 조회되었습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "소비 내역 전체 삭제", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 소비 내역이 삭제되었습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsumptionsDeleteAllResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "message": "전체 소비 내역이 삭제되었습니다."
                                    }
                                    """))),

            @ApiResponse(responseCode = "404", description = "소비 내역을 찾을 수 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "NOT_FOUND_ERROR",
                                    "message": "소비 내역을 찾을 수 없습니다."
                                    }
                                    """))),

            @ApiResponse(responseCode = "500", description = "소비 내역 삭제에 실패했습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "DATABASE_ERROR",
                                    "message": "소비 내역 삭제에 실패했습니다."
                                    }
                                    """)))
    })
    @DeleteMapping("delete/all/{access_url}")
    public ResponseEntity<ConsumptionsDeleteAllResponse> ConsumptionDeleteAll(@AuthenticationPrincipal String email, @PathVariable(name = "access_url") String accessUrl) {
        ConsumptionsDeleteAllResponse response = consumptionService.deleteAll(email, accessUrl);
        response.setMessage("전체 소비 내역이 삭제되었습니다.");

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "소비 내역 삭제(단일)", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소비 내역이 삭제되었습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsumptionsDeleteOneResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "message": "소비 내역이 삭제되었습니다."
                                    }
                                    """))),

            @ApiResponse(responseCode = "404", description = "해당 소비 내역을 찾을 수 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "NOT_FOUND_ERROR",
                                    "message": "해당 소비 내역을 찾을 수 없습니다."
                                    }
                                    """))),

            @ApiResponse(responseCode = "500", description = "소비 내역 삭제에 실패했습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "DATABASE_ERROR",
                                    "message": "소비 내역 삭제에 실패했습니다."
                                    }
                                    """)))
    })
    @DeleteMapping("delete/one/{id}")
    public ResponseEntity<ConsumptionsDeleteOneResponse> ConsumptionDeleteAll(@AuthenticationPrincipal String email, @PathVariable(name = "id") Long id) {
        ConsumptionsDeleteOneResponse response = consumptionService.deleteOne(email, id);
        response.setMessage("소비 내역이 삭제되었습니다.");

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "전체 기간 소비 분석", security = {@SecurityRequirement(name = "JWT")}, hidden = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 기간 소비 내역 분석이 완료되었습니다.",
                    content = @Content(mediaType = "apllication/json",
                            schema = @Schema(implementation = ConsumptionsSummaryResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "totalAmount": "300000",
                                    "byCategory": [{"name": "식품", "amount": "200000"}, {"name": "기타", "amount": "45000"}, {"name": "음료", "amount": "25000"}, {"name": "생활용품", "amount": "30000"}],
                                    "topExpenses": [{"name": "말보로레드", "amount": "45000"}],
                                    "storeExpenses": [{"name": "GS25", "amount": "100000"}, {"name": "맥도날드", "amount": "200000"}],
                                    "message": "전체 기간 소비 내역 분석이 완료되었습니다."
                                    }
                                    """)))
    })
    @GetMapping("/summary")
    public ResponseEntity<ConsumptionsSummaryResponse> consumptionSummary(@AuthenticationPrincipal String email) {
        ConsumptionsSummaryResponse response = ConsumptionsSummaryResponse.builder()
                .byCategory(consumptionService.getTotalAmountByEmailAndCategory(email, null, null, null))
                .totalAmount(consumptionService.getTotalAmountByEmail(email, null, null, null))
                .topExpenses(consumptionService.getMaxAmountByEmailAndItemName(email, null, null, null))
                .storeExpenses(consumptionService.getStoreExpenseListByEmailAndStoreName(email, null, null, null))
                .message("전체 기간 소비 내역 분석이 완료되었습니다.")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "특정 년도 기간 소비 분석", security = {@SecurityRequirement(name = "JWT")}, hidden = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "{year}년의 소비 내역 분석이 완료되었습니다.",
                    content = @Content(mediaType = "apllication/json",
                            schema = @Schema(implementation = ConsumptionsYearResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "totalAmount": "300000",
                                    "byCategory": [{"name": "식품", "amount": "200000"}, {"name": "기타", "amount": "45000"}, {"name": "음료", "amount": "25000"}, {"name": "생활용품", "amount": "30000"}],
                                    "topExpenses": [{"name": "말보로레드", "amount": "45000"}],
                                    "storeExpenses": [{"name": "GS25", "amount": "100000"}, {"name": "맥도날드", "amount": "200000"}],
                                    "message": "2015년도의 소비 내역 분석이 완료되었습니다."
                                    }
                                    """)))
    })
    @GetMapping("/{year}")
    public ResponseEntity<ConsumptionsYearResponse> consumptionYear(@AuthenticationPrincipal String email, @PathVariable("year") Long year) {
        ConsumptionsYearResponse response = ConsumptionsYearResponse.builder()
                .byCategory(consumptionService.getTotalAmountByEmailAndCategory(email, year, null, null))
                .topExpenses(consumptionService.getMaxAmountByEmailAndItemName(email, year, null, null))
                .totalAmount(consumptionService.getTotalAmountByEmail(email, year, null, null))
                .storeExpenses(consumptionService.getStoreExpenseListByEmailAndStoreName(email, year, null, null))
                .message(year + "년도의 소비 내역 분석이 완료되었습니다.")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "특정 년월 기간 소비 분석", security = {@SecurityRequirement(name = "JWT")}, hidden = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "{year}년 {month}월 소비 내역 분석이 완료되었습니다.",
                    content = @Content(mediaType = "apllication/json",
                            schema = @Schema(implementation = ConsumptionsMonthResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "totalAmount": "50000",
                                    "byCategory": [{"name": "식품", "amount": "20000"}, {"name": "기타", "amount": "18000"}, {"name": "교통/주유", "amount": "12000"}],
                                    "topExpenses": [{"name": "말보로레드", "amount": "18000"}],
                                    "storeExpenses": [{"name": "GS25", "amount": "30000"}, {"name": "맥도날드", "amount": "20000"}],
                                    "message": "2015년 11월 소비 내역 분석이 완료되었습니다."
                                    }
                                    """)))
    })
    @GetMapping("/{year}/{month}")
    public ResponseEntity<ConsumptionsMonthResponse> consumptionYearMonth(@AuthenticationPrincipal String email, @PathVariable("year") Long year, @PathVariable("month") Long month) {
        ConsumptionsMonthResponse response = ConsumptionsMonthResponse.builder()
                .byCategory(consumptionService.getTotalAmountByEmailAndCategory(email, year, month, null))
                .topExpenses(consumptionService.getMaxAmountByEmailAndItemName(email, year, month, null))
                .totalAmount(consumptionService.getTotalAmountByEmail(email, year, month, null))
                .storeExpenses(consumptionService.getStoreExpenseListByEmailAndStoreName(email, year, month, null))
                .message(year + "년 " + month + "월 소비 내역 분석이 완료되었습니다.")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "특정 년, 월, 일 소비 내역 분석", security = {@SecurityRequirement(name = "JWT")}, hidden = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "{year}년 {month}월 {day}일 소비 내역 분석이 완료되었습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsumptionsDayResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "totalAmount": "5000",
                                    "byCategory": [{"name": "식품", "amount": "500"}, {"name": "기타", "amount": "4500"}],
                                    "topExpenses": [{"name": "말보로레드", "amount": "4500"}],
                                    "storeExpenses": [{"name": "GS25", "amount": "5000"}],
                                    "message": "2015년 11월 19일 소비 내역 분석이 완료되었습니다."
                                    }
                                    """)))
    })
    @GetMapping("/{year}/{month}/{day}")
    public ResponseEntity<ConsumptionsDayResponse> consumptionsYearMonthDay(
            @AuthenticationPrincipal String email,
            @PathVariable("year") Long year,
            @PathVariable("month") Long month,
            @PathVariable("day") Long day) {
        ConsumptionsDayResponse response = ConsumptionsDayResponse.builder()
                .byCategory(consumptionService.getTotalAmountByEmailAndCategory(email, year, month, day))
                .topExpenses(consumptionService.getMaxAmountByEmailAndItemName(email, year, month, day))
                .totalAmount(consumptionService.getTotalAmountByEmail(email, year, month, day))
                .storeExpenses(consumptionService.getStoreExpenseListByEmailAndStoreName(email, year, month, day))
                .message(year + "년 " + month + "월 " + day + "일 소비 내역 분석이 완료되었습니다.")
                .build();

        return ResponseEntity.ok(response);
    }
}
