package backend.backend.repository;

import backend.backend.domain.FixedIncome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FixedIncomeRepository extends JpaRepository<FixedIncome, Long> {
    List<FixedIncome> findByEmail(String email);
    void deleteByEmail(String email);

    List<FixedIncome> findByDateLessThanEqual(LocalDate date);
}
