package dto.memo.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMemoResponse {
    private String message;

    public UpdateMemoResponse(String message) {
        this.message = message;
    }
}