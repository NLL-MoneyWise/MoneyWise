package backend.backend.service;

import backend.backend.domain.Memo;
import backend.backend.dto.memo.model.MemoDTO;
import backend.backend.exception.DatabaseException;
import backend.backend.exception.NotFoundException;
import backend.backend.repository.MemoRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class MemoService {

    private final MemoRepository memoRepository;

    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    public Long createMemo(String content, String userEmail) {
        Memo memo = new Memo();
        memo.setContent(content);
        memo.setEmail(userEmail);
        LocalDate date = LocalDate.now();
        memo.setDate(date);

        try {
            return memoRepository.save(memo).getId();
        } catch (DataAccessException e) {
            throw new DatabaseException("메모 저장 중 오류가 발생했습니다.");
        }
    }

    public void updateMemo(Long memoId, String content, String userEmail) {
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new NotFoundException(memoId + "번의 메모를 찾지 못했습니다."));

        memo.setContent(content);

        try {
            memoRepository.save(memo);
        } catch (DataAccessException e) {
            throw new DatabaseException("메모 저장 중 오류가 발생했습니다.");
        }
    }

    // 이메일을 기준으로 메모를 조회하는 메서드
    public List<MemoDTO> getAllMemos(String userEmail) {
        List<Memo> memos = memoRepository.findByEmail(userEmail);  // 이메일로 메모 조회
        List<MemoDTO> memoDTOList = new ArrayList<>();

        if (memos == null || memos.isEmpty()) {
            return Collections.emptyList();
        }

        for(Memo memo : memos) {
            MemoDTO memoDTO = new MemoDTO();
            memoDTO.setContent(memo.getContent());
            memoDTO.setId(memo.getId());
            memoDTO.setEmail(memo.getEmail());
            String date = memo.getDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            memoDTO.setDate(date);

            memoDTOList.add(memoDTO);
        }

        return memoDTOList;
    }
}
