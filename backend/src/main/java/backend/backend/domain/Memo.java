package backend.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMO_SEQ_GEN")
    @SequenceGenerator(name = "MEMO_SEQ_GEN", sequenceName = "memo_seq", allocationSize = 1, initialValue = 3000)
    private Long id; // 기본 키, 오라클에서 IDENTITY 전략 제외 (테이블에서 관리)

    @Column(nullable = false) // 이메일은 필수 입력 (외래 키, 테이블에서 관리)
    private String email;

    @Column(nullable = false, updatable = false, name = "memo_date") // 날짜는 필수 입력, 수정 불가 (테이블에서 관리)
    private LocalDate date;

    @Column(nullable = false) // 내용은 필수 입력 (테이블에서 관리)
    private String content;
}
