package backend.backend.repository;

import backend.backend.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
// Memo 엔티티에 대한 데이터베이스 작업을 처리하는 JPA 레포지토리
public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findByEmail(String email);
}// JpaRepository에서 기본적인 CRUD 메서드 제공

// JPA
// List의 검색 결과가 null이면 빈 리스트를 반환
// 단일 값일 경우 내부적으로 Optional.ofNullable()을 사용해서 Service에서 에러를 잡을 수 있게함