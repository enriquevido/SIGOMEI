package mx.uv.sigomei.exception;

import java.rmi.Remote;

public class CuentaBloqueadaException extends RuntimeException implements Remote {
    private static final long serialVersionUID = 1L;

    public CuentaBloqueadaException(String mensaje) {
        super(mensaje);
    }
}
