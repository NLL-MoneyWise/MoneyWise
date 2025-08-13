package backend.backend.dto.income.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncomeDto {
    private Long id;
    private Long amount;
    private String sortation;
    private String name;
    private String date;
}
