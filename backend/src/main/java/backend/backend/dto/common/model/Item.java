package backend.backend.dto.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Item {
    private String category;
    private String name;
    private Long amount;
    private Long quantity;
}
