package backend.backend.dto.fixedIncome.response;

import backend.backend.dto.common.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FixedIncomeFindOneResponse extends BaseResponse {
    private String name;
    private Long amount;
    private String date;
}
