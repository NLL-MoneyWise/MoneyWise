package backend.backend.dto.response;

import backend.backend.common.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private String status;
    private ErrorType error_type;
    private String message;
}
