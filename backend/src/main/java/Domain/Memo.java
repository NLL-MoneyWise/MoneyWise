package Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Memo {

    @Id
    private Long id; // 기본 키, 오라클에서 IDENTITY 전략 제외 (테이블에서 관리)

    @Column(nullable = false) // 이메일은 필수 입력 (외래 키, 테이블에서 관리)
    private String email;

    @Column(nullable = false, updatable = false) // 날짜는 필수 입력, 수정 불가 (테이블에서 관리)
    private LocalDateTime date;

    @Column(nullable = false) // 내용은 필수 입력 (테이블에서 관리)
    private String content;
}
