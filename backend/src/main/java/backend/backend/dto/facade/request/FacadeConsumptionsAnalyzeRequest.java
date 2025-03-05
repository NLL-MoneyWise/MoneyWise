package backend.backend.dto.facade.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FacadeConsumptionsAnalyzeRequest {
    private String period;
    private Long year;
    private Long month;
}
