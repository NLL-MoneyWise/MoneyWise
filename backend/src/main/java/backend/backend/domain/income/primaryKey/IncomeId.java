package backend.backend.domain.income.primaryKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
@Setter
public class IncomeId {
    @Column
    private String email;
    @Column(name = "income_date")
    private Long day;
}
