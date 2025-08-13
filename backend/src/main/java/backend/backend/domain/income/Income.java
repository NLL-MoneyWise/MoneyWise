package backend.backend.domain.income;

import backend.backend.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INCOME_SEQ_GEN")
    @SequenceGenerator(name = "INCOME_SEQ_GEN", sequenceName = "INCOME_SEQ", allocationSize = 1, initialValue = 1)
    private Long id;
    @Column(name = "email")
    private String email;
    @Column(name = "amount")
    private Long amount;
    @Column(name = "name")
    private String name;
    @Column(name = "income_date")
    private LocalDate income_date;
    @Column(name = "category_id")
    private String category_id;
    @Column(name = "sortation")
    private String sortation;

    @ManyToOne
    @JoinColumn(name = "email", referencedColumnName = "email", insertable = false, updatable = false)
    User user;
}
