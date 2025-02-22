package backend.backend.dto.memo.response;

import backend.backend.domain.Memo;
import backend.backend.dto.memo.model.MemoDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetAllMemosResponse {

    private String message;
    private List<MemoDTO> memoDTOList;

    // 생성자
    public GetAllMemosResponse(String message, List<MemoDTO> memoDTOList) {
        this.message = message;
        this.memoDTOList = memoDTOList;
    }
}
