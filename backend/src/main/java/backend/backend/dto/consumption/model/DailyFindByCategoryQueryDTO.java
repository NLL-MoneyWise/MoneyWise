package backend.backend.dto.consumption.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DailyFindByCategoryQueryDTO {
    private LocalDate date;
    private String name;
    private Long amount;
}
