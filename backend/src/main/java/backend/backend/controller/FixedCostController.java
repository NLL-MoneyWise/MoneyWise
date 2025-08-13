package backend.backend.controller;

import backend.backend.dto.fixedCost.request.FixedCostSaveRequest;
import backend.backend.dto.fixedCost.request.FixedCostUpdateRequest;
import backend.backend.dto.fixedCost.response.*;
import backend.backend.exception.response.ErrorResponse;
import backend.backend.service.FixedCostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fixedcost")
public class FixedCostController {
    private final FixedCostService fixedCostService;

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
        FixedCostSaveResponse response = fixedCostService.createFixedCost(email, request);
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
        FIxedCostUpdateResponse response = fixedCostService.updateFixedCost(email, request);
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
        FixedCostFindOneResponse response = fixedCostService.findOneFixedCost(email, id);
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
        FixedCostFindAllResponse response = fixedCostService.findAllFixedCost(email);
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
        FixedCostDeleteOneResponse response = fixedCostService.deleteOneFixedCost(email, id);
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
        FixedCostDeleteAllResponse response = fixedCostService.deleteAllFixedCost(email);
        response.setMessage("고정 지출액 삭제가 완료되었습니다.");

        return ResponseEntity.ok(response);
    }
}
