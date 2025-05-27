package backend.backend.repository;

import backend.backend.domain.fixedCost.FixedCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FixedCostRepository extends JpaRepository<FixedCost, Long>{
    List<FixedCost> findByEmail(String email);
    void deleteByEmail(String email);

    @Query("SELECT f FROM FixedCost f WHERE f.fixedCostDate <= :date")
    List<FixedCost> findByFixedCostDateBefore(@Param("date") LocalDate date);
}
