package backend.backend.dto.consumption.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TopExpense {
    private String name;
    private Long amount;
}