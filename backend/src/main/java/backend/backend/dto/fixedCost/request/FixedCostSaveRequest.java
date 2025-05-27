package backend.backend.dto.fixedCost.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FixedCostSaveRequest {
    private int day;
    private String category;
    private Long amount;
    private String name;
}
