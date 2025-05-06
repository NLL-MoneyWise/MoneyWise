package backend.backend.dto.consumption.request;

import backend.backend.dto.consumption.model.ConsumptionDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ConsumptionsUpdateRequest {
    private List<ConsumptionDTO> consumptionDTOList;
    private String store_name;
    private String date;
}
