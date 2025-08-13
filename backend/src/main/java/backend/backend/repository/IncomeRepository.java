package backend.backend.repository;

import backend.backend.domain.income.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findByEmail(String email);
    void deleteByEmail(String email);
}
