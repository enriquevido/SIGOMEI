package mx.uv.sigomei.exception;

public class TransicionEstadoException extends RuntimeException {
    public TransicionEstadoException(String message) {
        super(message);
    }

    public TransicionEstadoException(String message, Throwable cause) {
        super(message, cause);
    }
}
