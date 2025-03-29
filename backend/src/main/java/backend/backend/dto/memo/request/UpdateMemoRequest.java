package backend.backend.dto.memo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMemoRequest {
    private String date;
    private String content;
}