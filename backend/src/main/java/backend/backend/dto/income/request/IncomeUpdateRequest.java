package backend.backend.dto.income.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncomeUpdateRequest {
    private Long id;
    private Long amount;
    private String name;
    private String date;
}
