package backend.backend.dto.fixedIncome.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FixedIncomeUpdateRequest {
    private Long id;
    private String name;
    private Long amount;
    private String date;
}
