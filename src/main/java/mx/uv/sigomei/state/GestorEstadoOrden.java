package mx.uv.sigomei.state;

import mx.uv.sigomei.enums.EstadoOrden;

public interface GestorEstadoOrden {
    void validarTransicion(EstadoOrden actual, EstadoOrden nuevo);
}
