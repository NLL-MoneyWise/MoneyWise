package Repository;

import Domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
// Memo 엔티티에 대한 데이터베이스 작업을 처리하는 JPA 레포지토리
public interface MemoRepository extends JpaRepository<Memo, Long> {
}// JpaRepository에서 기본적인 CRUD 메서드 제공