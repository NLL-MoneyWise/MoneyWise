package backend.backend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECEIPT_SEQ_GEN")
    @SequenceGenerator(name = "RECEIPT_SEQ_GEN", sequenceName = "receipt_seq", allocationSize = 1, initialValue = 1000)
    private Long id;
    @Column(length = 100, nullable = true)
    private String email;
    @Column(nullable = true)
    private Long total_amount;
    @Column(nullable = true, name = "receipt_date")
    private LocalDate date;
    @Column(length = 1000, nullable = true)
    private String access_url;
}
