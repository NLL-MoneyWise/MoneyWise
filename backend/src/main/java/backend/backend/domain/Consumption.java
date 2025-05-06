package backend.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Consumption {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONSUMPTION_SEQ_GEN")
    @SequenceGenerator(name = "CONSUMPTION_SEQ_GEN", sequenceName = "consumption_seq", allocationSize = 1, initialValue = 2000)
    private Long id;
    @Column
    private String email;
    @Column
    private String access_url;
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
