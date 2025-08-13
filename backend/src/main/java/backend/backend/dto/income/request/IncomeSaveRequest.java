package backend.backend.dto.income.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncomeSaveRequest {
    private Long amount;
    private String name;
    private String date;
    private String sortation;
}
