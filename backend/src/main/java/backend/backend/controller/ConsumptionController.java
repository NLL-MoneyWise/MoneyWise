package backend.backend.controller;

import backend.backend.dto.consumption.request.ConsumptionsSaveRequest;
import backend.backend.dto.consumption.request.ConsumptionsUpdateRequest;
import backend.backend.dto.consumption.response.*;
import backend.backend.dto.fixedCost.request.FixedCostSaveRequest;
import backend.backend.dto.fixedCost.request.FixedCostUpdateRequest;
import backend.backend.dto.fixedCost.response.*;
import backend.backend.exception.response.ErrorResponse;
import backend.backend.scheduler.FixedCostScheduler;
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

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Consumptions")
@RestController
@RequiredArgsConstructor
@RequestMapping("/consumptions")
public class ConsumptionController {
    private final ConsumptionService consumptionService;

    @Operation(summary = "고정 지출액 저장", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "고정 지출액 저장이 완료되었습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FixedCostSaveResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "message": "고정 지출액 저장이 완료되었습니다."
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "day는 1과 28 사이의 값 이어야 합니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "VALIDATION_ERROR",
                                    "message": "day는 1과 29 사이의 값 이어야 합니다."
                                    }
                                    """))),
            @ApiResponse(responseCode = "500", description = "고정 지출액 저장에 실패했습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "DATABASE_ERROR",
                                    "message": "고정 지출액 저장에 실패했습니다."
                                    }
                                    """)))
    })
    @PostMapping("/fixed/save")
    public ResponseEntity<FixedCostSaveResponse> createFixedCost(@AuthenticationPrincipal String email, @RequestBody FixedCostSaveRequest request) {
        FixedCostSaveResponse response = consumptionService.createFixedCost(email, request);
        response.setMessage("고정 지출액 저장이 완료되었습니다.");

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "고정 지출액 수정", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "고정 지출액 변경이 완료되었습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FIxedCostUpdateResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "message": "고정 지출액 변경이 완료되었습니다."
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "해당하는 고정 지출을 찾을 수 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "NOT_FOUND_ERROR",
                                    "message": "해당하는 고정 지출을 찾을 수 없습니다."
                                    }
                                    """))),
            @ApiResponse(responseCode = "500", description = "고정 지출액 변경에 실패했습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "DATABASE_ERROR",
                                    "message": "고정 지출액 변경에 실패했습니다."
                                    }
                                    """)))
    })
    @PutMapping("/fixed/update")
    public ResponseEntity<FIxedCostUpdateResponse> updateFixedCost(@AuthenticationPrincipal String email, @RequestBody FixedCostUpdateRequest request) {
        FIxedCostUpdateResponse response = consumptionService.updateFixedCost(email, request);
        response.setMessage("고정 지출액 변경이 완료되었습니다.");

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "고정 지출액 조회(단일)", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "고정 지출액 조회가 완료되었습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FixedCostFindOneResponse.class),
                            examples = @ExampleObject("""
                                    {
                                      "amount": 100000,
                                      "category": "기타",
                                      "date": "2025-05-10",
                                      "name": "기타지출",
                                      "message": "고정 지출액 조회가 완료되었습니다."
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "해당하는 고정 지출을 찾을 수 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "NOT_FOUND_ERROR",
                                    "message": "해당하는 고정 지출을 찾을 수 없습니다."
                                    }
                                    """))),
            @ApiResponse(responseCode = "500", description = "고정 지출액 조회에 실패했습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "DATABASE_ERROR",
                                    "message": "고정 지출액 조회에 실패했습니다."
                                    }
                                    """)))
    })
    @GetMapping("/fixed/one/{id}")
    public ResponseEntity<FixedCostFindOneResponse> findOneFixedCost(@AuthenticationPrincipal String email, @PathVariable Long id) {
        FixedCostFindOneResponse response = consumptionService.findOneFixedCost(email, id);
        response.setMessage("고정 지출액 조회가 완료되었습니다.");

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "고정 지출액 전체 조회", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "고정 지출액 조회가 완료되었습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FixedCostFindAllResponse.class),
                            examples = @ExampleObject("""
                                    {
                                      "fixedCostDTOList": [
                                        {
                                          "id": 10005,
                                          "date": "2025-05-10",
                                          "category": "기타",
                                          "amount": 100000,
                                          "name": "기타지출"
                                        },
                                        {
                                          "id": 10006,
                                          "date": "2025-05-20",
                                          "category": "교통/주유",
                                          "amount": 50000,
                                          "name": "버스비"
                                        }
                                      ],
                                      "message": "고정 지출액 조회가 완료되었습니다."
                                    }
                                    """))),
            @ApiResponse(responseCode = "500", description = "고정 지출액 조회에 실패했습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "DATABASE_ERROR",
                                    "message": "고정 지출액 조회에 실패했습니다."
                                    }
                                    """)))
    })
    @GetMapping("/fixed/all")
    public ResponseEntity<FixedCostFindAllResponse> findAllFixedCost(@AuthenticationPrincipal String email) {
        FixedCostFindAllResponse response = consumptionService.findAllFixedCost(email);
        response.setMessage("고정 지출액 조회가 완료되었습니다.");

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "고정 지출액 삭제(단일)", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "고정 지출액 삭제가 완료되었습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FixedCostDeleteOneResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "message": "고정 지출액 삭제가 완료되었습니다."
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "해당하는 고정 지출을 찾을 수 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "NOT_FOUND_ERROR",
                                    "message": "해당하는 고정 지출을 찾을 수 없습니다."
                                    }
                                    """))),
            @ApiResponse(responseCode = "500", description = "고정 지출액 조회에 실패했습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "DATABASE_ERROR",
                                    "message": "고정 지출액 삭제에 실패했습니다."
                                    }
                                    """)))
    })
    @DeleteMapping("/fixed/one/{id}")
    public ResponseEntity<FixedCostDeleteOneResponse> deleteOneFixedCost(@AuthenticationPrincipal String email, @PathVariable Long id) {
        FixedCostDeleteOneResponse response = consumptionService.deleteOneFixedCost(email, id);
        response.setMessage("고정 지출액 삭제가 완료되었습니다.");

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "고정 지출액 전체 삭제", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "고정 지출액 삭제가 완료되었습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FixedCostDeleteAllResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "message": "고정 지출액 삭제가 완료되었습니다."
                                    }
                                    """))),
            @ApiResponse(responseCode = "500", description = "고정 지출액 조회에 실패했습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "DATABASE_ERROR",
                                    "message": "고정 지출액 삭제에 실패했습니다."
                                    }
                                    """)))
    })
    @DeleteMapping("/fixed/all")
    public ResponseEntity<FixedCostDeleteAllResponse> deleteOneFixedCost(@AuthenticationPrincipal String email) {
        FixedCostDeleteAllResponse response = consumptionService.deleteAllFixedCost(email);
        response.setMessage("고정 지출액 삭제가 완료되었습니다.");

        return ResponseEntity.ok(response);
    }

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
                                    "consumptionDTO": {"id": "2000", "category": "잡화", "name": "말보로레드", "amount": "4500", "quantity": "1"},
                                    "store_name": "CU",
                                    "date": "2025-05-06",
                                    "message": "2000번의 소비 내용이 조회되었습니다."
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
    @DeleteMapping("/delete/one/{id}")
    public ResponseEntity<ConsumptionsDeleteOneResponse> consumptionDeleteAll(@AuthenticationPrincipal String email, @PathVariable(name = "id") Long id) {
        ConsumptionsDeleteOneResponse response = consumptionService.deleteOne(email, id);
        response.setMessage("소비 내역이 삭제되었습니다.");

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "특정 기간 소비 일별 분석", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "2015년 11월의 일별 소비 분석이 완료되었습니다./2015년 11월 1일부터 10일 까지의 일별 소비 분석이 완료되었습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ConsumptionsDailyAnalyzeResponse.class),
                            examples = {
                                    @ExampleObject(name = "year과 month만 지정한 경우", value = """
                                            {
                                              "dailyList": [
                                                {
                                                  "totalAmount": 252300,
                                                  "date": "2015-11-01",
                                                  "byCategory": [
                                                    {
                                                      "name": "기타",
                                                      "amount": 250000
                                                    },
                                                    {
                                                      "name": "음료",
                                                      "amount": 2300
                                                    }
                                                  ],
                                                  "topExpenses": [
                                                    {
                                                      "name": "말보로레드보루",
                                                      "amount": 250000
                                                    }
                                                  ],
                                                  "storeExpenses": [
                                                    {
                                                      "name": "CU",
                                                      "amount": 250000
                                                    },
                                                    {
                                                      "name": "GS25",
                                                      "amount": 2300
                                                    }
                                                  ]
                                                },
                                                {
                                                  "totalAmount": 4500,
                                                  "date": "2015-11-20",
                                                  "byCategory": [
                                                    {
                                                      "name": "기타",
                                                      "amount": 4500
                                                    }
                                                  ],
                                                  "topExpenses": [
                                                    {
                                                      "name": "말보로레드",
                                                      "amount": 4500
                                                    }
                                                  ],
                                                  "storeExpenses": [
                                                    {
                                                      "name": "GS25",
                                                      "amount": 4500
                                                    }
                                                  ]
                                                }
                                              ],
                                              "message": "2015년 11월의 일별 소비 분석이 완료되었습니다."
                                            }
                                            """),
                                    @ExampleObject(name = "start_day와 last_day를 지정한 경우", value = """
                                            {
                                              "dailyList": [
                                                {
                                                  "totalAmount": 2300,
                                                  "date": "2015-11-15",
                                                  "byCategory": [
                                                    {
                                                      "name": "음료",
                                                      "amount": 2300
                                                    }
                                                  ],
                                                  "topExpenses": [
                                                    {
                                                      "name": "파워에이드",
                                                      "amount": 2300
                                                    }
                                                  ],
                                                  "storeExpenses": [
                                                    {
                                                      "name": "CU",
                                                      "amount": 2300
                                                    }
                                                  ]
                                                },
                                                {
                                                  "totalAmount": 4500,
                                                  "date": "2015-11-20",
                                                  "byCategory": [
                                                    {
                                                      "name": "기타",
                                                      "amount": 4500
                                                    }
                                                  ],
                                                  "topExpenses": [
                                                    {
                                                      "name": "말보로레드",
                                                      "amount": 4500
                                                    }
                                                  ],
                                                  "storeExpenses": [
                                                    {
                                                      "name": "GS25",
                                                      "amount": 4500
                                                    }
                                                  ]
                                                }
                                              ],
                                              "message": "2015년 11월 15일 부터 20일 까지의 일별 소비 분석이 완료되었습니다."
                                            }
                                            """)
                            })),

            @ApiResponse(responseCode = "500", description = "소비 분석에 실패했습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "DATABASE_ERROR",
                                    "message": "소비 분석에 실패했습니다."
                                    }
                                    """)))
    })
    @GetMapping("/analyze/daily")
    public ResponseEntity<ConsumptionsDailyAnalyzeResponse> consumptionDailyAnalyze(
            @AuthenticationPrincipal String email,
            @RequestParam(name = "year") Long year,
            @RequestParam(name = "month") Long month,
            @RequestParam(name = "start_day", required = false) Long startDay,
            @RequestParam(name = "last_day", required = false) Long lastDay) {
        ConsumptionsDailyAnalyzeResponse response = consumptionService.getDailyAnalyze(email, year, month, startDay, lastDay);
        if (startDay == null || lastDay == null) {
            response.setMessage(year + "년 " + month + "월의 일별 소비 분석이 완료되었습니다.");
        } else {
            response.setMessage(year + "년 " + month + "월 " + startDay + "일 부터 " + lastDay + "일 까지의 일별 소비 분석이 완료되었습니다.");
        }

        return ResponseEntity.ok(response);
    }

    /*
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
*/

    private final FixedCostScheduler fixedCostScheduler;

    @Operation(summary = "스케줄러 수동 실행", hidden = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "스케줄러 수동 실행 성공")
    })
    @PostMapping("/fixed-cost/process")
    public ResponseEntity<Map<String, Object>> testProcessFixedCosts() {
        Map<String, Object> result = new HashMap<>();

        try {
            // 스케줄러 메서드 직접 호출
            fixedCostScheduler.processFixedCosts();

            result.put("success", true);
            result.put("message", "고정지출 처리 테스트 완료");

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "처리 실패: " + e.getMessage());
            result.put("error", e.getClass().getSimpleName());

            return ResponseEntity.status(500).body(result);
        }
    }
}
