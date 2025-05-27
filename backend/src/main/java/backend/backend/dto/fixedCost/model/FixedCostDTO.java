package backend.backend.dto.fixedCost.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FixedCostDTO {
    private Long id;
    private String date;
    private String category;
    private Long amount;
    private String name;
}
