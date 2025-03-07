package backend.backend.dto.memo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MemoDTO {
    private Long id;
    private String email;
    private String date;
    private String content;
}
