package mx.uv.sigomei.state;

import mx.uv.sigomei.enums.EstadoOrden;
import mx.uv.sigomei.exception.TransicionEstadoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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

    @Test
    @DisplayName("RN-08 debe permitir la transicion valida de PROGRAMADA a EN_EJECUCION")
    void rn08_debePermitirTransicionProgramadaAEnEjecucion() {
        // Given
        EstadoOrden actual = EstadoOrden.PROGRAMADA;
        EstadoOrden nuevo = EstadoOrden.EN_EJECUCION;

        // When / Then
        assertDoesNotThrow(() -> gestorEstadoOrden.validarTransicion(actual, nuevo));
        assertNotEquals(actual, nuevo,
                "Los estados origen y destino deben ser distintos");
        assertEquals(EstadoOrden.EN_EJECUCION, nuevo,
                "El estado destino es exactamente EN_EJECUCION");
    }
}
