package backend.backend.dto.consumption.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsumptionFindAccessUrlDTO {
    private Long id;
    private String category;
    private String name;
    private Long amount;
    private Long quantity;
}
