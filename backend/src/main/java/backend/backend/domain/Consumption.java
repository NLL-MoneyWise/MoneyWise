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
    @Column(name = "access_url")
    private String accessUrl;
    @Column
    private Long category_id;
    @Column(name = "name")
    private String name;
    @Column
    private Long amount;
    @Column
    private LocalDate consumption_date;
    @Column
    private String storeName;
    @Column
    private Long quantity;
    @Column
    private String sortation;
}
