package mx.uv.sigomei.state;

import mx.uv.sigomei.enums.EstadoOrden;
import mx.uv.sigomei.exception.TransicionEstadoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class GestorEstadoOrdenTest {
    private final GestorEstadoOrden gestorEstadoOrden = new GestorEstadoOrdenImpl();

    @Test
    @DisplayName("RN-08 debe rechazar transicion de FINALIZADA a EN_EJECUCION")
    void rn08_debeRechazarTransicionFinalizadaAEnEjecucion() {
        // Given
        EstadoOrden actual = EstadoOrden.FINALIZADA;
        EstadoOrden nuevo = EstadoOrden.EN_EJECUCION;

        // When / Then
        assertThrows(TransicionEstadoException.class, () -> gestorEstadoOrden.validarTransicion(actual, nuevo));
    }
}
