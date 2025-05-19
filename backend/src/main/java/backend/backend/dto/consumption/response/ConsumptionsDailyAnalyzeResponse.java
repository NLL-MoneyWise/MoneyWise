package backend.backend.dto.consumption.response;


import backend.backend.dto.consumption.model.DailyList;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ConsumptionsDailyAnalyzeResponse {
    private List<DailyList> dailyList;
    private String message;
}
