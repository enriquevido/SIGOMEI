package mx.uv.sigomei.exception;

public class ValidacionException extends RuntimeException {
    public ValidacionException(String message) {
        super(message);
    }

    public ValidacionException(String message, Throwable cause) {
        super(message, cause);
    }
}
