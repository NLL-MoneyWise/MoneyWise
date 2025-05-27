package backend.backend.dto.fixedCost.response;

import backend.backend.dto.fixedCost.model.FixedCostDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FixedCostFindAllResponse {
    private List<FixedCostDTO> fixedCostDTOList;
    private String message;
}
