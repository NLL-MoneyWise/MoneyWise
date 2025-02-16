package Controller;

import Domain.Memo;
import Service.MemoService;
import backend.backend.security.jwt.JwtUtils;
import dto.memo.request.CreateMemoRequest;
import dto.memo.request.UpdateMemoRequest;
import dto.memo.response.CreateMemoResponse;
import dto.memo.response.GetAllMemosResponse;
import dto.memo.response.UpdateMemoResponse;
import org.springframework.http.ResponseEntity;
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
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody CreateMemoRequest request) {
        String token = bearerToken.substring(7);
        String email = jwtUtils.getUserEmailFromToken(token);
        Memo memo = memoService.createMemo(request.getContent(), email);
        return ResponseEntity.ok(new CreateMemoResponse(memo.getId(), "Memo successfully created"));
    }

    @PutMapping("/{memoId}")
    public ResponseEntity<UpdateMemoResponse> updateMemo(
            @RequestHeader("Authorization") String bearerToken,
            @PathVariable Long memoId,
            @RequestBody UpdateMemoRequest request) {
        String token = bearerToken.substring(7);
        String email = jwtUtils.getUserEmailFromToken(token);
        memoService.updateMemo(memoId, request.getContent(), email);
        return ResponseEntity.ok(new UpdateMemoResponse("Memo successfully updated"));
    }
    // 메모 조회 기능 추가
    @GetMapping("/find")
    public ResponseEntity<GetAllMemosResponse> getAllMemos(
            @RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.substring(7);
        String email = jwtUtils.getUserEmailFromToken(token);

        // 사용자의 모든 메모를 조회
        List<Memo> memos = memoService.getAllMemos(email);

        // 응답 객체 생성 후 반환
        if (!memos.isEmpty()) {
            return ResponseEntity.ok(new GetAllMemosResponse("Success", memos));
       } else {
            return ResponseEntity.ok(new GetAllMemosResponse("No memos found", memos));
        }
    }

}
