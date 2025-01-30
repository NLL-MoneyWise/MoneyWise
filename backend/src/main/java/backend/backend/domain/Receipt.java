package backend.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
@Builder
public class Receipt {
    @Id
    private Long id;
    @Column(length = 100, nullable = true)
    private String email;
    @Column(nullable = true)
    private Long total_amount;
    @Column(nullable = true)
    private LocalDate date;
    @Column(length = 1000, nullable = true)
    private String access_url;
}
