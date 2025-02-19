package backend.backend.controller;

import backend.backend.domain.Memo;
import backend.backend.service.MemoService;
import backend.backend.security.jwt.JwtUtils;
import backend.backend.dto.memo.request.CreateMemoRequest;
import backend.backend.dto.memo.request.UpdateMemoRequest;
import backend.backend.dto.memo.response.CreateMemoResponse;
import backend.backend.dto.memo.response.GetAllMemosResponse;
import backend.backend.dto.memo.response.UpdateMemoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memos")
public class MemoController {

    private final MemoService memoService;
    private final JwtUtils jwtUtils;

    public MemoController(MemoService memoService, JwtUtils jwtUtils) {
        this.memoService = memoService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/save")
    public ResponseEntity<CreateMemoResponse> createMemo(
            @AuthenticationPrincipal String email,
            @RequestBody CreateMemoRequest request) {
        Memo memo = memoService.createMemo(request.getContent(), email);
        return ResponseEntity.ok(new CreateMemoResponse(memo.getId(), "메모가 저장되었습니다."));
    }

    @PutMapping("/{memoId}")
    public ResponseEntity<UpdateMemoResponse> updateMemo(
            @AuthenticationPrincipal String email,
            @PathVariable Long memoId,
            @RequestBody UpdateMemoRequest request) {
        memoService.updateMemo(memoId, request.getContent(), email);
        return ResponseEntity.ok(new UpdateMemoResponse("메모가 수정되었습니다."));
    }
    // 메모 조회 기능 추가
    @GetMapping("/find")
    public ResponseEntity<GetAllMemosResponse> getAllMemos(
            @AuthenticationPrincipal String email) {
        // 사용자의 모든 메모를 조회
        List<Memo> memos = memoService.getAllMemos(email);

        // 응답 객체 생성 후 반환
        if (!memos.isEmpty()) {
            return ResponseEntity.ok(new GetAllMemosResponse("모든 메모 내역을 불러왔습니다.", memos));
       } else {
            return ResponseEntity.ok(new GetAllMemosResponse("메모 내역이 없습니다.", memos));
        }
    }

}
