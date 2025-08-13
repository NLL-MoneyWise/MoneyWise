package backend.backend.dto.fixedIncome.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FixedIncomeSaveRequest {
    private String name;
    private Long amount;
    private String date;
}
