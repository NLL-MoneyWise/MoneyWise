package backend.backend.domain.fixedCost;

import backend.backend.domain.Category;
import backend.backend.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "FIXEDCOST")
@Getter
@Setter
public class FixedCost {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FIXEDCOST_SEQ_GEN")
    @SequenceGenerator(name = "FIXEDCOST_SEQ_GEN", sequenceName = "FIXEDCOST_SEQ", allocationSize = 1, initialValue = 10000)
    private Long id;

    @Column(name = "category_id")
    private Long categoryId;

    @Column
    private String email;

    @Column
    private Long amount;

    @Column
    private String name;

    @Column(name = "FIXED_COST_DATE")
    private LocalDate fixedCostDate;

    @ManyToOne
    @JoinColumn(name = "email", referencedColumnName = "email", insertable = false, updatable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "category_Id", referencedColumnName = "id", insertable = false, updatable = false)
    Category category;
}
