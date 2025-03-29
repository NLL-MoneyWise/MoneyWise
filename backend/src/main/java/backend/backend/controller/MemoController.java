package backend.backend.controller;

import backend.backend.dto.memo.model.MemoDTO;
import backend.backend.exception.response.ErrorResponse;
import backend.backend.service.MemoService;
import backend.backend.dto.memo.request.CreateMemoRequest;
import backend.backend.dto.memo.request.UpdateMemoRequest;
import backend.backend.dto.memo.response.CreateMemoResponse;
import backend.backend.dto.memo.response.GetAllMemosResponse;
import backend.backend.dto.memo.response.UpdateMemoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Memo Relevant")
@RestController
@RequestMapping("/api/memos")
public class MemoController {

    private final MemoService memoService;

    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    @Operation(summary = "메모 저장", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메모가 저장되었습니다.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = CreateMemoResponse.class),
            examples = @ExampleObject("""
                    {
                    "id": "3001",
                    "message": "메모가 저장되었습니다."
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

            @ApiResponse(responseCode = "409", description = "해당 날짜에 메모가 이미 존재합니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject("""
                    {
                    "typeName": "CONFLICT_ERROR",
                    "message": "해당 날짜에 메모가 이미 존재합니다."
                    }
                    """))),

            @ApiResponse(responseCode = "500", description = "메모 저장 중 오류가 발생했습니다.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class),
            examples = @ExampleObject("""
                    {
                    "typeName": "DATABASE_ERROR",
                    "message": "메모 저장 중 오류가 발생했습니다."
                    }
                    """)))
    })
    @PostMapping("/save")
    public ResponseEntity<CreateMemoResponse> createMemo(
            @AuthenticationPrincipal String email,
            @RequestBody CreateMemoRequest request) {
        memoService.createMemo(email, request);
        CreateMemoResponse response = new CreateMemoResponse("메모가 저장되었습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "메모 수정", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메모가 수정되었습니다.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = UpdateMemoResponse.class),
            examples = @ExampleObject("""
                    {
                    "message": "메모가 수정되었습니다."
                    }
                    """))),

            @ApiResponse(responseCode = "404", description = "{id}번의 메모를 찾지 못했습니다.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class),
            examples = @ExampleObject("""
                    {
                    "typeName": "NOT_FOUND_ERROR",
                    "message": "3001번의 메모를 찾지 못했습니다."
                    }
                    """))),

            @ApiResponse(responseCode = "500", description = "메모 저장 중 오류가 발생했습니다.",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ErrorResponse.class),
            examples = @ExampleObject("""
                    {
                    "typeName": "DATABASE_ERROR",
                    "message": "메모 저장 중 오류가 발생했습니다."
                    }
                    """)))
    })
    @PutMapping("/{memoId}")
    public ResponseEntity<UpdateMemoResponse> updateMemo(
            @AuthenticationPrincipal String email,
            @RequestBody UpdateMemoRequest request) {
        memoService.updateMemo(request, email);
        return ResponseEntity.ok(new UpdateMemoResponse("메모가 수정되었습니다."));
    }

    // 메모 조회 기능 추가
    @Operation(summary = "사용자의 모든 메모 조회", security = {@SecurityRequirement(name = "JWT")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 메모 내역을 불러왔습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetAllMemosResponse.class),
                            examples = @ExampleObject("""
                                    {
                                    "memoDTOList": [
                                    {"id": "3001", "content": "텅장", "memo_date": "2025/02/09"},
                                    {"id": "3002", "content": "텅텅장", "memo_date": "2025/02/10"}
                                    ],
                                    "message": "모든 메모 내역을 불러왔습니다."
                                    }
                                    """)))
    })
    @GetMapping("/find")
    public ResponseEntity<GetAllMemosResponse> getAllMemos(
            @AuthenticationPrincipal String email) {
        // 사용자의 모든 메모를 조회
        List<MemoDTO> memoDTOList = memoService.getAllMemos(email);

        return ResponseEntity.ok(new GetAllMemosResponse("모든 메모 내역을 불러왔습니다.", memoDTOList));
    }
}
