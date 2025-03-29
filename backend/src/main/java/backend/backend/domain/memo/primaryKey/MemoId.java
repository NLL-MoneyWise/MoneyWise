package backend.backend.domain.memo.primaryKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class MemoId implements Serializable {
    private String email;
    @Column(name = "memo_date")
    private LocalDate date;
}
