package backend.backend.repository;

import backend.backend.dto.consumption.model.ByCategory;
import backend.backend.dto.consumption.model.StoreExpense;
import backend.backend.dto.consumption.model.TopExpense;

import java.util.List;
import java.util.Optional;

public interface ConsumptionRepositoryCustom {
    List<ByCategory> findByCategoryAndEmail(String email, Long year, Long month, Long day);

    List<TopExpense> findTopExpenseByEmail(String email, Long year, Long month, Long day);

    List<StoreExpense> findStoreExpenseByStoreName(String email, Long year, Long month, Long day);

    Optional<Long> sumAmountByEmail(String email, Long year, Long month, Long day);
}
