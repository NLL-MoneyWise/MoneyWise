package backend.backend.domain.income;

import backend.backend.domain.User;
import backend.backend.domain.income.primaryKey.IncomeId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Income {
    @EmbeddedId
    private IncomeId id;
    @Column
    private Long cost;

    @ManyToOne
    @JoinColumn(name = "email", referencedColumnName = "email", insertable = false, updatable = false)
    User user;
}
