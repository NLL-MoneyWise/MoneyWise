package Service;

import Domain.Memo;
import Repository.MemoRepository;
import backend.Exception.MemoNotFoundException;
import backend.Exception.GeneralException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional // 서비스 계층에서 트랜잭션 관리
public class MemoService {

    private final MemoRepository memoRepository;

    public MemoService(MemoRepository memoRepository) { // MemoRepository 주입
        this.memoRepository = memoRepository;
    }

    public Memo createMemo(String content, String userEmail) { // 메모 생성 메서드
        try {
            Memo memo = new Memo(); // 새 Memo 객체 생성
            memo.setContent(content); // 내용 설정
            memo.setEmail(userEmail); // 사용자 이메일 설정
            return memoRepository.save(memo); // DB에 저장
        } catch (Exception e) {
            throw new GeneralException("Failed to create memo: " + e.getMessage());
        }
    }

    public Memo updateMemo(Long memoId, String content, String userEmail) { // 메모 수정 메서드
        try {
            Memo memo = memoRepository.findById(memoId) // ID로 메모 검색, 없으면 예외 발생
                    .orElseThrow(() -> new MemoNotFoundException("Memo not found with id: " + memoId));

            // 이메일 확인 (권한 체크)
            if (!memo.getEmail().equals(userEmail)) {
                throw new GeneralException("You do not have permission to modify this memo.");
            }

            memo.setContent(content); // 내용 업데이트
            return memoRepository.save(memo); // DB에 저장
        } catch (MemoNotFoundException e) {
            throw e; // MemoNotFoundException은 그대로 던짐
        } catch (Exception e) {
            throw new GeneralException("Failed to update memo: " + e.getMessage());
        }
    }

    public List<Memo> getAllMemos() { // 모든 메모 조회 메서드
        try {
            return memoRepository.findAll(); // DB에서 모든 메모 조회
        } catch (Exception e) {
            throw new GeneralException("Failed to retrieve memos: " + e.getMessage());
        }
    }
}
