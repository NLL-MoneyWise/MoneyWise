package backend.backend.repository;

import backend.backend.dto.consumption.model.*;

import java.util.List;
import java.util.Optional;

public interface ConsumptionRepositoryCustom {
    List<ByCategory> findByCategoryAndEmail(String email, Long year, Long month, Long day);

    List<TopExpense> findTopExpenseByEmail(String email, Long year, Long month, Long day);

    List<StoreExpense> findStoreExpenseByStoreName(String email, Long year, Long month, Long day);

    Optional<Long> sumAmountByEmail(String email, Long year, Long month, Long day);

    List<DailySumAmountQueryDTO> dailySumAmountByEmail(String email, Long year, Long month, Long startDay, Long lastDay);

    List<DailyFindByCategoryQueryDTO> dailyFindByCategoryAndEmail(String email, Long year, Long month, Long startDay, Long lastDay);

    List<DailyFindTopExpenseQueryDTO> dailyFindTopExpenseByEmail(String email, Long year, Long month, Long startDay, Long lastDay);

    List<DailyFindStoreExpenseQueryDTO> dailyFindStoreExpenseByStoreName(String email, Long year, Long month, Long startDay, Long lastDay);
}
