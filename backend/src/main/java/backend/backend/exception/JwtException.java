package backend.backend.exception;

public class JwtException extends RuntimeException {
    public JwtException(String message) {
        super(message);
    }
}
