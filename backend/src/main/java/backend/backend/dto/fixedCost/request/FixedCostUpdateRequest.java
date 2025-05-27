package backend.backend.dto.fixedCost.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FixedCostUpdateRequest {
    private Long id;
    private Long amount;
}
