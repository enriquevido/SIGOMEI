package mx.uv.sigomei.state;

import mx.uv.sigomei.enums.EstadoOrden;

public class GestorEstadoOrdenImpl implements GestorEstadoOrden {
    @Override
    public void validarTransicion(EstadoOrden actual, EstadoOrden nuevo) {
        throw new UnsupportedOperationException("Pendiente fase VERDE TDD");
    }
}
