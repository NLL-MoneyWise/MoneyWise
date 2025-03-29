package backend.backend.dto.memo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMemoRequest {
    private String date;
    private String content;
}