package backend.backend.dto.consumption.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ConsumptionDTO {
    private Long id;
    private String category;
    private String name;
    private Long amount;
    private Long quantity;
}
