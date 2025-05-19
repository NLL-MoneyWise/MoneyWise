package backend.backend.dto.consumption.model;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ByCategory {
    private String name;
    private Long amount;
}
