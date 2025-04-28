package backend.backend.service;

import backend.backend.domain.memo.Memo;
import backend.backend.domain.memo.primaryKey.MemoId;
import backend.backend.dto.memo.model.MemoDTO;
import backend.backend.dto.memo.request.CreateMemoRequest;
import backend.backend.dto.memo.request.DeleteMemoRequest;
import backend.backend.dto.memo.request.UpdateMemoRequest;
import backend.backend.exception.ConflictException;
import backend.backend.exception.DatabaseException;
import backend.backend.exception.NotFoundException;
import backend.backend.exception.ValidationException;
import backend.backend.repository.MemoRepository;
import org.springframework.cglib.core.Local;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    public void createMemo(String email, CreateMemoRequest request) {
        LocalDate date = LocalDate.now();

        if (request.getDate() != null) {
            try {
                date = LocalDate.parse(request.getDate());
            } catch (DateTimeParseException e) {
                throw new ValidationException("잘못된 날짜 형식입니다. yyyy-MM-dd를 사용해주세요.");
            }
        }

        MemoId memoId = new MemoId(email, date);
        if (memoRepository.existsById(memoId)) {
            throw new ConflictException("해당 날짜에 메모가 이미 존재합니다.");
        }

        Memo memo = new Memo();
        memo.setId(memoId);
        memo.setContent(request.getContent());

        try {
            memoRepository.save(memo);
        } catch (DataAccessException e) {
            throw new DatabaseException("메모 저장 중 오류가 발생했습니다.");
        }
    }

    public void updateMemo(UpdateMemoRequest request, String email) {
        if (request.getDate() == null) {
            throw new ValidationException("날짜는 필수 입력입니다.");
        }

        try {
            LocalDate date = LocalDate.parse(request.getDate());
            MemoId memoId = new MemoId(email, date);

            Memo memo = memoRepository.findById(memoId)
                    .orElseThrow(() -> new NotFoundException("해당 날짜의 메모를 찾지 못했습니다."));

            memo.setContent(request.getContent());

            memoRepository.save(memo);
        } catch (DataAccessException e) {
            throw new DatabaseException("메모 저장 중 오류가 발생했습니다.");
        } catch (DateTimeParseException e) {
            throw new ValidationException("잘못된 날짜 형식입니다. yyyy-MM-dd를 사용해주세요.");
        }
    }

    // 이메일을 기준으로 메모를 조회하는 메서드
    public List<MemoDTO> getAllMemos(String email) {
        List<Memo> memos = memoRepository.findByIdEmail(email);  // 이메일로 메모 조회
        List<MemoDTO> memoDTOList = new ArrayList<>();

        if (memos == null || memos.isEmpty()) {
            return Collections.emptyList();
        }

        for(Memo memo : memos) {
            MemoDTO memoDTO = new MemoDTO();
            String date = memo.getId().getDate().toString();
            memoDTO.setContent(memo.getContent());
            memoDTO.setEmail(memo.getId().getEmail());
            memoDTO.setDate(date);

            memoDTOList.add(memoDTO);
        }

        return memoDTOList;
    }

    public void deleteMemo(String email, DeleteMemoRequest request) {
        if (request.getDate() == null) {
            throw new ValidationException("날짜는 필수 입력입니다.");
        }

        try {
            LocalDate date = LocalDate.parse(request.getDate());
            MemoId id = new MemoId();
            id.setDate(date);
            id.setEmail(email);

            memoRepository.deleteById(id);

        } catch (DateTimeParseException e) {
            throw new ValidationException("잘못된 날짜 형식입니다. yyyy-MM-dd를 사용해주세요.");
        } catch (DataAccessException e) {
            throw new DatabaseException("메모 삭제에 실패했습니다.");
        }
    }
}
