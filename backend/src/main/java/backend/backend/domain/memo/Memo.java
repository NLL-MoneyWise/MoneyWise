package backend.backend.domain.memo;

import backend.backend.domain.User;
import backend.backend.domain.memo.primaryKey.MemoId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Memo {

    @EmbeddedId
    private MemoId id;

    @Column(nullable = false) // 내용은 필수 입력 (테이블에서 관리)
    private String content;

    @ManyToOne
    @JoinColumn(name = "email", referencedColumnName = "email", insertable = false, updatable = false)
    User user;
}
