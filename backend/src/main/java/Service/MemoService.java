package Service;

import Domain.Memo;
import Repository.MemoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional // 서비스 계층에서 트랜잭션 관리
public class MemoService {

    private final MemoRepository memoRepository;

    public MemoService(MemoRepository memoRepository) {// MemoRepository 주입
        this.memoRepository = memoRepository;
    }

    public Memo createMemo(String content) { // 메모 생성 메서드
        Memo memo = new Memo(); // 새 Memo 객체 생성
        memo.setContent(content); // 내용 설정
        return memoRepository.save(memo); // DB에 저장
    }

    public Memo updateMemo(Long memoId, String content) { // 메모 수정 메서드
        Memo memo = memoRepository.findById(memoId) // ID로 메모 검색, 없으면 예외 발생
                .orElseThrow(() -> new IllegalArgumentException("Memo not found with id: " + memoId));
        memo.setContent(content); // 내용 업데이트
        return memoRepository.save(memo); // DB에 저장
    }

    public List<Memo> getAllMemos() { // 모든 메모 조회 메서드
        return memoRepository.findAll(); // DB에서 모든 메모 조회
    }
}

