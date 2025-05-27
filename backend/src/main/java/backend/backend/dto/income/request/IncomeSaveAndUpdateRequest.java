package backend.backend.dto.income.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncomeSaveAndUpdateRequest {
    private Long day;
    private Long cost;
}
