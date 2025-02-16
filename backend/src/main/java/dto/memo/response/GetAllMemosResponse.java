package dto.memo.response;

import Domain.Memo;
import java.util.List;

public class GetAllMemosResponse {

    private String message;
    private List<Memo> memos;

    // 생성자
    public GetAllMemosResponse(String message, List<Memo> memos) {
        this.message = message;
        this.memos = memos;
    }

    // Getter & Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Memo> getMemos() {
        return memos;
    }

    public void setMemos(List<Memo> memos) {
        this.memos = memos;
    }
}
