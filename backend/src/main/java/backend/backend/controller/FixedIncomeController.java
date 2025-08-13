package backend.backend.controller;

import backend.backend.dto.common.response.BaseResponse;
import backend.backend.dto.fixedIncome.request.FixedIncomeSaveRequest;
import backend.backend.dto.fixedIncome.request.FixedIncomeUpdateRequest;
import backend.backend.dto.fixedIncome.response.FixedIncomeFindAllResponse;
import backend.backend.dto.fixedIncome.response.FixedIncomeFindOneResponse;
import backend.backend.exception.response.ErrorResponse;
import backend.backend.scheduler.FixedIncomeScheduler;
import backend.backend.service.FixedIncomeService;
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

@RestController
@RequestMapping("/fixed-incomes")
@RequiredArgsConstructor
@Tag(name = "고정 소득 관련 기능", description = "date는 다음 결제일 입니다. 생성시 이미 지난 날짜를 입력하면 다음 날에 반영되고 다음 결제일이 1달 증가합니다.")
public class FixedIncomeController {
    private final FixedIncomeService fixedIncomeService;
    private final FixedIncomeScheduler scheduler;

    @PostMapping("/execute-scheduler")
    public BaseResponse executeScheduler() {
        scheduler.executeManually();

        BaseResponse response = new BaseResponse();
        response.setMessage("스케줄러를 수동 실행 했습니다.");
        return response;
    }

    @Operation(summary = "고정 소득 생성", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "고정 소득 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(name = "생성 성공", value = """
                                    {
                                        "message": "고정 소득이 생성되었습니다."
                                    }
                                    """))),

            @ApiResponse(responseCode = "400", description = "날짜 형식 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "day가 28보다 큰 경우", value = """
                                            {
                                                "typeName": "VALIDATION_ERROR",
                                                "message": "day는 1에서 28 사이의 값을 입력해주세요."
                                            }
                                            """),
                                    @ExampleObject(name = "day가 1보다 작거나 yyyy-MM-dd 형식이 아닐 경우", value = """
                                            {
                                                "typeName": "VALIDATION_ERROR",
                                                "message": "잘못된 날짜 형식입니다."
                                            }
                                            """),
                                    @ExampleObject(name = "현재 년도 및 월 이전의 날짜를 입력한 경우", value = """
                                            {
                                                "typeName": "VALIDATION_ERROR",
                                                "message": "현재 월 이전의 날짜는 입력할 수 없습니다."
                                            }
                                            """)
                            })),

            @ApiResponse(responseCode = "500", description = "데이터베이스 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "저장 실패", value = """
                                    {
                                        "typeName": "DATABASE_ERROR",
                                        "message": "저장에 실패했습니다."
                                    }
                                    """)))
    })
    @PostMapping
    public ResponseEntity<BaseResponse> createFixedIncome(@AuthenticationPrincipal String email, @RequestBody FixedIncomeSaveRequest request) {
        BaseResponse response = fixedIncomeService.create(email, request);
        response.setMessage("고정 소득이 생성되었습니다.");

        return ResponseEntity.ok(response);
    }


    @Operation(summary = "고정 소득 수정", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "고정 소득 수정 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(name = "수정 성공", value = """
                                    {
                                        "message": "고정 소득이 수정되었습니다."
                                    }
                                    """))),

            @ApiResponse(responseCode = "400", description = "날짜 형식 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "day가 28보다 큰 경우", value = """
                                            {
                                                "typeName": "VALIDATION_ERROR",
                                                "message": "day는 1에서 28 사이의 값을 입력해주세요."
                                            }
                                            """),
                                    @ExampleObject(name = "day가 1보다 작거나 yyyy-MM-dd 형식이 아닐 경우", value = """
                                            {
                                                "typeName": "VALIDATION_ERROR",
                                                "message": "잘못된 날짜 형식입니다."
                                            }
                                            """),
                                    @ExampleObject(name = "현재 년도 및 월 이전의 날짜를 입력한 경우", value = """
                                            {
                                                "typeName": "VALIDATION_ERROR",
                                                "message": "현재 월 이전의 날짜는 입력할 수 없습니다."
                                            }
                                            """)
                            })),

            @ApiResponse(responseCode = "401", description = "데이터에 대한 접근 권한이 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "요청한 email과 데이터의 email이 다름", value = """
                                    {
                                    "typeName": "AUTH_ERROR",
                                    "message": "수정 권한이 없습니다."
                                    }
                                    """))),

            @ApiResponse(responseCode = "404", description = "찾을 수 없는 데이터",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "요청한 id에 대한 데이터가 없음", value = """
                                    {
                                    "typeName": "NOT_FOUND_ERROR",
                                    "message": "해당하는 고정 소득을 찾을 수 없습니다."
                                    }
                                    """))),

            @ApiResponse(responseCode = "500", description = "데이터베이스 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "저장 실패", value = """
                                    {
                                        "typeName": "DATABASE_ERROR",
                                        "message": "저장에 실패했습니다."
                                    }
                                    """)))
    })
    @PutMapping
    public ResponseEntity<BaseResponse> updateFixedIncome(@AuthenticationPrincipal String email, @RequestBody FixedIncomeUpdateRequest request) {
        BaseResponse response = fixedIncomeService.update(email, request);
        response.setMessage("고정 소득이 수정되었습니다.");

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "id로 고정 소득 조회", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "고정 소득 내역을 불려왔습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FixedIncomeFindOneResponse.class),
                            examples = @ExampleObject(name = "조회 성공", value = """
                                    {
                                        "message": "고정 소득 내역을 불려왔습니다.",
                                        "name": "월급",
                                        "amount": "2000000",
                                        "date": "2025-08-10"
                                    }
                                    """))),

            @ApiResponse(responseCode = "401", description = "데이터에 대한 접근 권한이 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "요청한 email과 데이터의 email이 다름", value = """
                                    {
                                    "typeName": "AUTH_ERROR",
                                    "message": "접근 권한이 없습니다."
                                    }
                                    """))),

            @ApiResponse(responseCode = "404", description = "찾을 수 없는 데이터",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "요청한 id에 대한 데이터가 없음", value = """
                                    {
                                    "typeName": "NOT_FOUND_ERROR",
                                    "message": "해당하는 고정 소득을 찾을 수 없습니다."
                                    }
                                    """)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<FixedIncomeFindOneResponse> findOneFixedIncome(@AuthenticationPrincipal String email, @PathVariable Long id) {
        FixedIncomeFindOneResponse response = fixedIncomeService.findOne(email, id);
        response.setMessage("고정 소득 내역을 불려왔습니다.");

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "email로 고정 소득 조회", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "고정 소득 내역을 불려왔습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FixedIncomeFindAllResponse.class),
                            examples = @ExampleObject(name = "조회 성공", value = """
                                    {
                                        "message": "모든 고정 소득 내역을 불러왔습니다.",
                                          "fixedIncomeDtoList": [
                                            {
                                              "id": 1,
                                              "name": "월급",
                                              "amount": 2000000,
                                              "date": "2025-08-10"
                                            },
                                            {
                                              "id": 2,
                                              "name": "부업",
                                              "amount": 1000000,
                                              "date": "2025-08-10"
                                            }
                                          ]
                                    }
                                    """))),

            @ApiResponse(responseCode = "500", description = "데이터베이스 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "조회 실패", value = """
                                    {
                                        "typeName": "DATABASE_ERROR",
                                        "message": "조회에 실패했습니다."
                                    }
                                    """)))
    })
    @GetMapping("/all")
    public ResponseEntity<FixedIncomeFindAllResponse> findAllFixedIncome(@AuthenticationPrincipal String email) {
        FixedIncomeFindAllResponse response = fixedIncomeService.findAll(email);
        response.setMessage("모든 고정 소득 내역을 불러왔습니다.");

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "id로 고정 소득 삭제", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "고정 소득 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(name = "삭제 성공", value = """
                                    {
                                        "message": "삭제가 완료되었습니다."
                                    }
                                    """))),

            @ApiResponse(responseCode = "401", description = "데이터에 대한 접근 권한이 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "요청한 email과 데이터의 email이 다름", value = """
                                    {
                                    "typeName": "AUTH_ERROR",
                                    "message": "접근 권한이 없습니다."
                                    }
                                    """))),

            @ApiResponse(responseCode = "404", description = "찾을 수 없는 데이터",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "요청한 id에 대한 데이터가 없음", value = """
                                    {
                                        "typeName": "NOT_FOUND_ERROR",
                                        "message": "해당하는 고정 소득을 찾을 수 없습니다."
                                    }
                                    """))),

            @ApiResponse(responseCode = "500", description = "데이터베이스 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "삭제 실패", value = """
                                    {
                                        "typeName": "DATABASE_ERROR",
                                        "message": "삭제에 실패했습니다."
                                    }
                                    """)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteOneFixedIncome(@AuthenticationPrincipal String email, @PathVariable Long id) {
        BaseResponse response = fixedIncomeService.deleteOne(email, id);
        response.setMessage("삭제가 완료되었습니다.");

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "email로 고정 소득 삭제", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "고정 소득 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(name = "삭제 성공", value = """
                                    {
                                        "message": "삭제가 완료되었습니다."
                                    }
                                    """))),

            @ApiResponse(responseCode = "500", description = "데이터베이스 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "삭제 실패", value = """
                                    {
                                        "typeName": "DATABASE_ERROR",
                                        "message": "삭제에 실패했습니다."
                                    }
                                    """)))
    })
    @DeleteMapping("/all")
    public ResponseEntity<BaseResponse> deleteAllFixedIncome(@AuthenticationPrincipal String email) {
        BaseResponse response = fixedIncomeService.deleteAll(email);
        response.setMessage("삭제가 완료되었습니다.");

        return ResponseEntity.ok(response);
    }
}
