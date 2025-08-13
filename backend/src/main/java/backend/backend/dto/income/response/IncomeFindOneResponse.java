package backend.backend.dto.income.response;

import backend.backend.dto.common.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class IncomeFindOneResponse extends BaseResponse {
    private Long amount;
    private String name;
    private String date;
    private String sortation;
}
