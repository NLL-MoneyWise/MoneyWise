package backend.backend.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@Setter
public class Consumption {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONSUMPTION_SEQ_GEN")
    @SequenceGenerator(name = "CONSUMPTION_SEQ_GEN", sequenceName = "consumption_seq", allocationSize = 1, initialValue = 2000)
    private Long id;
    @Column
    private String email;
    @Column
    private Long receipt_id;
    @Column
    private Long category_id;
    @Column
    private String item_name;
    @Column
    private Long amount;
    @Column
    private LocalDate consumption_date;
    @Column
    private String storeName;
    @Column
    private Long quantity;
}
