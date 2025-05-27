package backend.backend.repository;

import backend.backend.domain.income.Income;
import backend.backend.domain.income.primaryKey.IncomeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, IncomeId> {
    List<Income> findById_Email(String email);

    void deleteById_Email(@Param("email") String email);
}
