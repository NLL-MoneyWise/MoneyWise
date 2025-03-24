package backend.backend.dto.consumption.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreExpense {
    String name;
    Long amount;
}
