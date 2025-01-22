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
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 증가 설정
    private Long id;

    @Column(nullable = false) // 이메일은 필수 입력 (외래 키)
    private String email;

    @Column(nullable = false, updatable = false) // 날짜는 필수 입력, 수정 불가
    private LocalDateTime date;

    @Column(nullable = false) // 내용은 필수 입력
    private String content;

    // @PrePersist // 엔티티가 저장되기 전에 실행: 생성 시간과 수정 시간을 현재 시간으로 설정
    // protected void onCreate() {
    // this.createdAt = LocalDateTime.now();
    // this.updatedAt = LocalDateTime.now();
    // }

    // @PreUpdate // 엔티티가 업데이트되기 전에 실행: 수정 시간을 현재 시간으로 설정
    // protected void onUpdate() {
    // this.updatedAt = LocalDateTime.now();
    // }
}
