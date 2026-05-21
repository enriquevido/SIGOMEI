package mx.uv.sigomei.validator;

import mx.uv.sigomei.domain.Equipo;
import mx.uv.sigomei.domain.OrdenMantenimiento;
import mx.uv.sigomei.domain.Tecnico;
import mx.uv.sigomei.enums.Criticidad;
import mx.uv.sigomei.enums.EstadoOrden;
import mx.uv.sigomei.enums.EstatusTecnico;
import mx.uv.sigomei.enums.NivelCertificacion;
import mx.uv.sigomei.enums.TipoEquipo;
import mx.uv.sigomei.exception.ReglaNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidadorReglasTest {
    private final ValidadorReglas validador = new ValidadorReglasImpl();

    @Test
    @DisplayName("RN-01 debe rechazar tecnico con especialidad distinta al tipo de equipo")
    void rn01_debeRechazarTecnicoConEspecialidadDistintaAlTipoEquipo() {
        // Given
        Equipo equipo = equipo(TipoEquipo.MECANICO, Criticidad.MEDIA);
        Tecnico tecnico = tecnico(TipoEquipo.ELECTRICO, NivelCertificacion.II, EstatusTecnico.ACTIVO);

        // When / Then
        assertThrows(ReglaNegocioException.class, () -> validador.validarEspecialidad(equipo, tecnico));
    }

    @Test
    @DisplayName("RN-02 debe rechazar orden activa duplicada para el mismo equipo y fecha")
    void rn02_debeRechazarOrdenActivaDuplicadaMismaFecha() {
        // Given
        Equipo equipo = equipo(TipoEquipo.MECANICO, Criticidad.MEDIA);
        LocalDate fecha = LocalDate.of(2026, 6, 1);
        OrdenMantenimiento existente = orden(equipo, fecha, null, null, EstadoOrden.PROGRAMADA);

        // When / Then
        assertThrows(ReglaNegocioException.class,
                () -> validador.validarOrdenActivaDuplicada(equipo, fecha, List.of(existente)));
    }

    @Test
    @DisplayName("RN-03 debe rechazar tecnico inactivo")
    void rn03_debeRechazarTecnicoInactivo() {
        // Given
        Tecnico tecnico = tecnico(TipoEquipo.HIDRAULICO, NivelCertificacion.II, EstatusTecnico.INACTIVO);

        // When / Then
        assertThrows(ReglaNegocioException.class, () -> validador.validarTecnicoActivo(tecnico));
    }

    @Test
    @DisplayName("RN-04 debe rechazar eliminacion con ordenes registradas")
    void rn04_debeRechazarEliminacionConOrdenesRegistradas() {
        // Given
        Equipo equipo = equipo(TipoEquipo.ELECTRICO, Criticidad.BAJA);
        OrdenMantenimiento orden = orden(equipo, LocalDate.of(2026, 6, 1), null, null, EstadoOrden.PROGRAMADA);

        // When / Then
        assertThrows(ReglaNegocioException.class, () -> validador.validarEliminacionSinOrdenes(List.of(orden)));
    }

    @Test
    @DisplayName("RN-04 debe permitir eliminacion sin ordenes registradas")
    void rn04_debePermitirEliminacionSinOrdenesRegistradas() {
        // When / Then
        assertDoesNotThrow(() -> validador.validarEliminacionSinOrdenes(List.of()));
    }

    @Test
    @DisplayName("RN-05 debe rechazar fechas incoherentes")
    void rn05_debeRechazarFechasIncoherentes() {
        // Given
        Equipo equipo = equipo(TipoEquipo.INSTRUMENTACION, Criticidad.MEDIA);
        OrdenMantenimiento orden = orden(
                equipo,
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 5),
                LocalDate.of(2026, 6, 3),
                EstadoOrden.EN_EJECUCION
        );

        // When / Then
        assertThrows(ReglaNegocioException.class, () -> validador.validarFechas(orden));
    }

    @Test
    @DisplayName("RN-05 debe permitir fechas coherentes")
    void rn05_debePermitirFechasCoherentes() {
        // Given
        Equipo equipo = equipo(TipoEquipo.INSTRUMENTACION, Criticidad.MEDIA);
        OrdenMantenimiento orden = orden(
                equipo,
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 2),
                LocalDate.of(2026, 6, 5),
                EstadoOrden.EN_EJECUCION
        );

        // When / Then
        assertDoesNotThrow(() -> validador.validarFechas(orden));
    }

    @Test
    @DisplayName("RN-06 debe rechazar orden finalizada sin costo real o fecha de cierre")
    void rn06_debeRechazarOrdenFinalizadaSinCostoRealOFechaCierre() {
        // Given
        Equipo equipo = equipo(TipoEquipo.MECANICO, Criticidad.ALTA);
        OrdenMantenimiento orden = orden(
                equipo,
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 2),
                null,
                EstadoOrden.FINALIZADA
        );
        orden.setCostoReal(null);

        // When / Then
        assertThrows(ReglaNegocioException.class, () -> validador.validarDatosFinalizacion(orden));
    }

    @Test
    @DisplayName("RN-06 debe permitir orden finalizada con costo real y fecha de cierre")
    void rn06_debePermitirOrdenFinalizadaConCostoRealYFechaCierre() {
        // Given
        Equipo equipo = equipo(TipoEquipo.MECANICO, Criticidad.ALTA);
        OrdenMantenimiento orden = orden(
                equipo,
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 2),
                LocalDate.of(2026, 6, 3),
                EstadoOrden.FINALIZADA
        );
        orden.setCostoReal(BigDecimal.valueOf(1300));

        // When / Then
        assertDoesNotThrow(() -> validador.validarDatosFinalizacion(orden));
    }

    @Test
    @DisplayName("RN-07 debe rechazar criticidad alta con tecnico certificacion I")
    void rn07_debeRechazarCriticidadAltaConTecnicoCertificacionI() {
        // Given
        Equipo equipo = equipo(TipoEquipo.ELECTRICO, Criticidad.ALTA);
        Tecnico tecnico = tecnico(TipoEquipo.ELECTRICO, NivelCertificacion.I, EstatusTecnico.ACTIVO);

        // When / Then
        assertThrows(ReglaNegocioException.class, () -> validador.validarCriticidadAlta(equipo, tecnico));
    }

    private Equipo equipo(TipoEquipo tipo, Criticidad criticidad) {
        return new Equipo(
                1L,
                "EQ-01",
                tipo,
                "Marca",
                "Modelo",
                "NS-001",
                "Planta A",
                LocalDate.of(2024, 1, 10),
                "Operativo",
                criticidad
        );
    }

    private Tecnico tecnico(TipoEquipo especialidad, NivelCertificacion nivelCertificacion, EstatusTecnico estatus) {
        return new Tecnico(
                1L,
                "Tecnico SIGOMEI",
                "RFC010101AB1",
                "2280000000",
                "tecnico@sigomei.mx",
                especialidad,
                nivelCertificacion,
                LocalDate.of(2023, 1, 1),
                estatus
        );
    }

    private OrdenMantenimiento orden(Equipo equipo, LocalDate fechaProgramada, LocalDate fechaInicio,
                                     LocalDate fechaCierre, EstadoOrden estadoOrden) {
        return new OrdenMantenimiento(
                1L,
                equipo,
                null,
                "Preventivo",
                fechaProgramada,
                fechaInicio,
                fechaCierre,
                "Revision general",
                BigDecimal.valueOf(1500),
                BigDecimal.valueOf(1300),
                estadoOrden
        );
    }
}
