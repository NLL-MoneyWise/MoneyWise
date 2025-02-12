package backend.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
public class Category {
    @Id
    private Long id;
    @Column
    private String name;
}
