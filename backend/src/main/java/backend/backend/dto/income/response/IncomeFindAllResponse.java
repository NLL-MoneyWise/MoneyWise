package backend.backend.dto.income.response;

import backend.backend.dto.common.response.BaseResponse;
import backend.backend.dto.income.model.IncomeDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IncomeFindAllResponse extends BaseResponse {
    private List<IncomeDto> incomeDtoList;
}
