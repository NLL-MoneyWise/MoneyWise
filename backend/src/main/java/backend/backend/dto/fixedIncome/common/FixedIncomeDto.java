package backend.backend.dto.fixedIncome.common;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FixedIncomeDto {
    private Long id;
    private String name;
    private Long amount;
    private String date;
}
