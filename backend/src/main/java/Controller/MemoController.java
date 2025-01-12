package Controller;

import Domain.Memo;
import Service.MemoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memos") // 메모와 관련된 API 경로
public class MemoController {

    private final MemoService memoService;

    public MemoController(MemoService memoService) { // MemoService 주입
        this.memoService = memoService;
    }

    @PostMapping // 메모 생성 API
    public ResponseEntity<Memo> createMemo(@RequestBody CreateMemoRequest request) {
        Memo memo = memoService.createMemo(request.getContent()); // 요청으로부터 content를 받아 메모 생성
        return ResponseEntity.ok(memo); // 생성된 메모 반환
    }

    @PutMapping("/{memoId}") // 메모 수정 API
    public ResponseEntity<Memo> updateMemo(
            @PathVariable Long memoId, // URL 경로에서 메모 ID 추출
            @RequestBody UpdateMemoRequest request) { // 요청으로부터 content 추출
        Memo memo = memoService.updateMemo(memoId, request.getContent());  // 메모 수정
        return ResponseEntity.ok(memo); // 수정된 메모 반환
    }

    @GetMapping // 모든 메모 조회 API
    public ResponseEntity<List<Memo>> getAllMemos() {
        List<Memo> memos = memoService.getAllMemos();  // 모든 메모 조회
        return ResponseEntity.ok(memos); // 조회된 메모 목록 반환
    }
}

class CreateMemoRequest { // 메모 생성 요청 객체
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}

class UpdateMemoRequest { // 메모 수정 요청 객체
    private String content;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}

