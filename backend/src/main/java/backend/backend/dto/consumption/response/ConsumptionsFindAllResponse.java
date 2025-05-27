package backend.backend.dto.consumption.response;

import backend.backend.dto.consumption.model.ConsumptionDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ConsumptionsFindAllResponse {
    private List<ConsumptionDTO> consumptionDTOList;
    private String message;
}
