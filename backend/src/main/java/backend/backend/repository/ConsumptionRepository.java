package backend.backend.repository;

import backend.backend.domain.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsumptionRepository extends JpaRepository<Consumption, Long>, ConsumptionRepositoryCustom {
//    @Query("SELECT SUM(c.amount) FROM Consumption c WHERE c.email = :email")
//    Optional<Long> sumAmountByEmail(String email);
    //@Query는 JPQL이므로 테이블 이름이 아닌 객체를 사용해야한다.
//    @Query("SELECT SUM(c.amount) FROM Consumption c WHERE c.email = :email AND EXTRACT(YEAR FROM c.consumption_date) = :year")
//    Optional<Long> sumAmountByEmailAndYear(String email, Long year);
//
//    @Query("SELECT SUM(c.amount) FROM Consumption c WHERE c.email = :email AND EXTRACT(YEAR FROM c.consumption_date) = :year AND EXTRACT(MONTH FROM c.consumption_date) = :month")
//    Optional<Long> sumAmountByEmailAndYearAndMonth(String email, Long year, Long month);

    List<Consumption> findByEmailAndAccessUrl(String email, String accessUrl);

    int deleteByEmailAndAccessUrl(String email, String accessUrl);

    int deleteByEmailAndId(String email, Long id);

    @Query("SELECT SUM(c.amount) FROM Consumption c WHERE c.accessUrl = :accessUrl")
    Optional<Long> sumAmountByAccessUrl(@Param("accessUrl") String accessUrl);
}
