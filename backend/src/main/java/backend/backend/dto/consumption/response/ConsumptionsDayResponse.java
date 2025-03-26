package backend.backend.dto.consumption.response;

import backend.backend.dto.consumption.model.ByCategory;
import backend.backend.dto.consumption.model.StoreExpense;
import backend.backend.dto.consumption.model.TopExpense;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsumptionsDayResponse {
    private Long totalAmount;
    private List<ByCategory> byCategory;
    private List<TopExpense> topExpenses;

    private List<StoreExpense> storeExpenses;
    private String message;
}
