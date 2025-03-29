package backend.backend.dto.memo.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMemoResponse {
    private String message;

    public CreateMemoResponse(String message) {
        this.message = message;
    }
}