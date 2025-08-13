package backend.backend.dto.fixedIncome.response;

import backend.backend.dto.common.response.BaseResponse;
import backend.backend.dto.fixedIncome.common.FixedIncomeDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FixedIncomeFindAllResponse extends BaseResponse {
    private List<FixedIncomeDto> FixedIncomeDtoList;
}
