package backend.backend.dto.facade.response;

import backend.backend.dto.consumption.model.ByCategory;
import backend.backend.dto.consumption.model.TopExpense;
import lombok.*;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacadeConsumptionsAnalyzeResponse {
    private Long totalAmount;
    private List<ByCategory> byCategory;
    private List<TopExpense> topExpenses;
    private String message;
}
