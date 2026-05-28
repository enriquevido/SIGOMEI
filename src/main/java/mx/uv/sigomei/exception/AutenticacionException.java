package mx.uv.sigomei.exception;

import java.rmi.Remote;

public class AutenticacionException extends RuntimeException implements Remote {
    private static final long serialVersionUID = 1L;

    public AutenticacionException(String mensaje) {
        super(mensaje);
    }
}
