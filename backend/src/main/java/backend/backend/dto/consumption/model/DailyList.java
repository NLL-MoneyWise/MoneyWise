package backend.backend.dto.consumption.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DailyList {
    private Long totalAmount;
    private String date;
    private List<ByCategory> byCategory;
    private List<TopExpense> topExpenses;
    private List<StoreExpense> storeExpenses;
}
