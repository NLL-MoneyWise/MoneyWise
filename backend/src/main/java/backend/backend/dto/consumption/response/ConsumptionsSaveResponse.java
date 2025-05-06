package backend.backend.dto.consumption.response;


import backend.backend.dto.consumption.model.ConsumptionDTO;
import lombok.*;

import java.util.List;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsumptionsSaveResponse {
    private List<ConsumptionDTO> consumptionDTOList;
    private String message;
}
