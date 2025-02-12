package backend.backend.dto.consumption.response;

import backend.backend.dto.consumption.model.ByCategory;
import backend.backend.dto.consumption.model.TopExpense;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsumptionsSummaryResponse {
    private Long totalAmount;
    private List<ByCategory> byCategory;
    private List<TopExpense> topExpenses;
    private String message;
}
