package backend.backend.repository;

import backend.backend.domain.Consumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsumptionRepository extends JpaRepository<Consumption, Long>, ConsumptionRepositoryCustom {
    Optional<Long> sumAmountByEmail(String email);
}
