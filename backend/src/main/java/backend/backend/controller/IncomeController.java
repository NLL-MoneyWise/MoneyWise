package backend.backend.controller;

import backend.backend.dto.income.request.IncomeSaveAndUpdateRequest;
import backend.backend.dto.income.response.*;
import backend.backend.exception.response.ErrorResponse;
import backend.backend.service.IncomeService;
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

@Tag(name = "Income")
@RestController
@RequiredArgsConstructor
@RequestMapping("/income")
public class IncomeController {
    private final IncomeService incomeService;

    @Operation(summary = "소득 내역 저장", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "소득 내역이 저장되었습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncomeSaveAndUpdateResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "message": "소득 내역이 저장되었습니다."
                                    }
                                    """))),

            @ApiResponse(responseCode = "400", description = "day 값 에러",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "VALIDATION_ERROR",
                                    "message": "잘못된 day 값 입니다."
                                    }
                                    """))),

            @ApiResponse(responseCode = "500", description = "저장 중 DB 에러",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "DATABASE_ERROR",
                                    "message": "소득 저장에 실패했습니다."
                                    }
                                    """)))
    })
    @PostMapping("/create-update")
    public ResponseEntity<IncomeSaveAndUpdateResponse> createAndupdateIncome(@AuthenticationPrincipal String email, @RequestBody IncomeSaveAndUpdateRequest request) {
        IncomeSaveAndUpdateResponse response = incomeService.save(email, request);
        response.setMessage("소득 내역이 저장되었습니다.");

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "모든 소득액 조회", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 내역을 불러왔습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncomeFindAllResponse.class),
                            examples = @ExampleObject("""
                                    {
                                      "incomeDTOList": [
                                        {
                                          "day": 10,
                                          "cost": 2000000
                                        },
                                        {
                                          "day": 15,
                                          "cost": 1000000
                                        }
                                      ],
                                      "message": "모든 내역을 불러왔습니다."
                                    }
                                    """))),
            @ApiResponse(responseCode = "500", description = "조회중 DB 에러",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "DATABASE_ERROR",
                                    "message": "조회에 실패했습니다."
                                    }
                                    """)))
    })
    @GetMapping("/find/all")
    public ResponseEntity<IncomeFindAllResponse> findAll(@AuthenticationPrincipal String email) {
        IncomeFindAllResponse response = incomeService.findAll(email);
        response.setMessage("모든 내역을 불러왔습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "소득액 조회(단일)", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회가 완료되었습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncomeFindOneResponse.class),
                            examples = @ExampleObject("""
                                    {
                                        "cost": "50000".
                                        "message": "조회가 완료되었습니다."
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "day 값 에러",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "VALIDATION_ERROR",
                                    "message": "잘못된 day 값 입니다."
                                    }
                                    """))),
            @ApiResponse(responseCode = "404", description = "id로 조회 실패",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "NOT_FOUND_ERROR",
                                    "message": "해당하는 소득액을 찾을 수 없습니다."
                                    }
                                    """))),
            @ApiResponse(responseCode = "500", description = "조회 중 DB 에러",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "DATABASE_ERROR",
                                    "message": "조회에 실패했습니다."
                                    }
                                    """)))
    })
    @GetMapping("/find/one/{day}")
    public ResponseEntity<IncomeFindOneResponse> findOne(@AuthenticationPrincipal String email, @PathVariable Long day) {
        IncomeFindOneResponse response = incomeService.findOne(email, day);
        response.setMessage("조회가 완료되었습니다.");

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "모든 소득액 삭제", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 내역이 삭제되었습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncomeDeleteAllResponse.class),
                            examples = @ExampleObject("""
                                    {
                                        "message": "모든 내역이 삭제되었습니다."
                                    }
                                    """))),
            @ApiResponse(responseCode = "500", description = "삭제 중 DB 에러",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "DATABASE_ERROR",
                                    "message": "삭제에 실패했습니다."
                                    }
                                    """)))
    })
    @DeleteMapping("/delete/all")
    public ResponseEntity<IncomeDeleteAllResponse> deleteAll(@AuthenticationPrincipal String email) {
        IncomeDeleteAllResponse response = incomeService.deleteAll(email);
        response.setMessage("모든 내역이 삭제되었습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "소득액 삭제(단일)", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제가 완료되었습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncomeDeleteOneResponse.class),
                            examples = @ExampleObject("""
                                    {
                                        "message": "삭제가 완료되었습니다."
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "day 값 에러",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "VALIDATION_ERROR",
                                    "message": "잘못된 day 값 입니다."
                                    }
                                    """))),
            @ApiResponse(responseCode = "500", description = "삭제 중 DB 에러",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "typeName": "DATABASE_ERROR",
                                    "message": "삭제에 실패했습니다."
                                    }
                                    """)))
    })
    @DeleteMapping("/delete/one/{day}")
    public ResponseEntity<IncomeDeleteOneResponse> deleteOne(@AuthenticationPrincipal String email, @PathVariable Long day) {
        IncomeDeleteOneResponse response = incomeService.deleteOne(email, day);
        response.setMessage("삭제가 완료되었습니다.");

        return ResponseEntity.ok(response);
    }
}
