package backend.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class FixedIncome {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIXEDINCOME_SEQ_GEN")
    @SequenceGenerator(name = "FIXEDINCOME_SEQ_GEN", sequenceName = "FIXEDINCOME_SEQ", allocationSize = 1, initialValue = 1)
    private Long id;
    @Column(name = "email")
    private String email;
    @Column(name = "name")
    private String name;
    @Column(name = "amount")
    private Long amount;
    @Column(name = "fixed_income_date")
    private LocalDate date;
}
