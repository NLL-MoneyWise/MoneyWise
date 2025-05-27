package backend.backend.dto.consumption.request;

import backend.backend.dto.consumption.model.ConsumptionFindAccessUrlDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ConsumptionsUpdateRequest {
    private List<ConsumptionFindAccessUrlDTO> consumptionDTOList;
    private String store_name;
    private String date;
}
