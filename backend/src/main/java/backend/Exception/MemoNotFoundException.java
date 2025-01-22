package backend.Exception;

public class MemoNotFoundException extends RuntimeException {
    public MemoNotFoundException(String message) {
        super(message);
    }
}