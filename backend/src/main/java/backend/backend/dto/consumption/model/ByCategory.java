package backend.backend.dto.consumption.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ByCategory {
    private String name;
    private Long amount;
}
