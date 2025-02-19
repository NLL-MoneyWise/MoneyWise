package Service;

import Domain.Memo;
import Repository.MemoRepository;
import backend.Exception.MemoNotFoundException;
import backend.Exception.GeneralException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemoService {

    private final MemoRepository memoRepository;

    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    public Memo createMemo(String content, String userEmail) {
        Memo memo = new Memo();
        memo.setContent(content);
        memo.setEmail(userEmail);
        return memoRepository.save(memo);
    }

    public Memo updateMemo(Long memoId, String content, String userEmail) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new MemoNotFoundException("Memo not found with id: " + memoId));

        if (!memo.getEmail().equals(userEmail)) {
            throw new GeneralException("You do not have permission to modify this memo.");
        }

        memo.setContent(content);
        return memoRepository.save(memo);
    }

    // 이메일을 기준으로 메모를 조회하는 메서드
    public List<Memo> getAllMemos(String userEmail) {
        return memoRepository.findByEmail(userEmail);  // 이메일로 메모 조회
    }
}
