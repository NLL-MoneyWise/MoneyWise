package backend.backend.dto.memo.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMemoResponse {
    private Long id;
    private String message;

    public CreateMemoResponse(Long id, String message) {
        this.id = id;
        this.message = message;
    }
}