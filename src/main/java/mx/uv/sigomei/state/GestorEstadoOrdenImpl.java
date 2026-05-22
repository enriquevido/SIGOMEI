package mx.uv.sigomei.state;

import mx.uv.sigomei.enums.EstadoOrden;
import mx.uv.sigomei.exception.TransicionEstadoException;

public class GestorEstadoOrdenImpl implements GestorEstadoOrden {

    @Override
    public void validarTransicion(EstadoOrden actual, EstadoOrden nuevo) {
        if (actual == null || nuevo == null) {
            throw new TransicionEstadoException("RN-08: el estado actual y el estado nuevo son obligatorios");
        }

        boolean permitida = switch (actual) {
            case PROGRAMADA -> nuevo == EstadoOrden.EN_EJECUCION || nuevo == EstadoOrden.CANCELADA;
            case EN_EJECUCION -> nuevo == EstadoOrden.FINALIZADA || nuevo == EstadoOrden.CANCELADA;
            case FINALIZADA, CANCELADA -> false;
        };

        if (!permitida) {
            throw new TransicionEstadoException("RN-08: transicion no permitida: " + actual + " -> " + nuevo);
        }
    }
}