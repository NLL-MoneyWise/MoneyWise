package backend.backend.repository;

import backend.backend.dto.consumption.model.ByCategory;
import backend.backend.dto.consumption.model.TopExpense;

import java.util.List;
import java.util.Optional;

public interface ConsumptionRepositoryCustom {
    List<ByCategory> findByCategoryAndEmail(String email, Long year, Long month);

    List<TopExpense> findTopExpenseByEmail(String email, Long year, Long month);

    Optional<Long> sumAmountByEmailAndYearAndMonthToQuerydsl(String email, Long year, Long month);
}
