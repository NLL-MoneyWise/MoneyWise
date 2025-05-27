package backend.backend.dto.consumption.response;

import backend.backend.dto.consumption.model.ConsumptionFindAccessUrlDTO;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ConsumptionsFindOneResponse {
    private ConsumptionFindAccessUrlDTO consumptionDTO;
    private String store_name;
    private String date;
    private String message;
}
