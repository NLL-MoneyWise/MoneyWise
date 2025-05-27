package backend.backend.dto.fixedCost.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FixedCostFindOneResponse {
    private Long amount;
    private String category;
    private String date;
    private String name;
    private String message;
}
