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
    @Column(length = 1000, name = "access_url")
    private String accessUrl;
    @Column(length = 100)
    private String email;
    @Column(nullable = true, name = "total_amount")
    private Long totalAmount;
    @Column(nullable = true, name = "receipt_date")
    private LocalDate date;
}
