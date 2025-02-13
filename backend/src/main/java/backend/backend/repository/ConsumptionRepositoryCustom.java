package backend.backend.repository;

import backend.backend.dto.consumption.model.ByCategory;
import backend.backend.dto.consumption.model.TopExpense;

import java.util.List;

public interface ConsumptionRepositoryCustom {
    List<ByCategory> findByCategoryAndEmail(String email, Long year);
    List<TopExpense> findTopExpenseByEmail(String email, Long year);
}
