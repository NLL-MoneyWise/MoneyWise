package backend.backend.dto.response;

import backend.backend.common.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private String typeName;
    private String message;
}
