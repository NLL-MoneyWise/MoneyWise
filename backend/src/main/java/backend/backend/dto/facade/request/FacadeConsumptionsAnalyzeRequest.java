package backend.backend.dto.facade.request;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class FacadeConsumptionsAnalyzeRequest {
    private String period;
    private Long year;
    private Long month;
}
