package backend.backend.dto.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorResponse {
    private String status;
    private String error_type;
    private String message;
}
