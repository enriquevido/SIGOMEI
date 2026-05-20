package mx.uv.sigomei.exception;

public class EntidadDuplicadaException extends RuntimeException {
    public EntidadDuplicadaException(String message) {
        super(message);
    }

    public EntidadDuplicadaException(String message, Throwable cause) {
        super(message, cause);
    }
}
