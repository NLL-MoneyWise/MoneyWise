package backend.backend.dto.income.response;

import backend.backend.dto.income.model.IncomeDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IncomeFindAllResponse {
    private List<IncomeDTO> incomeDTOList;
    private String message;
}
