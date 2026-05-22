package mx.uv.sigomei.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import mx.uv.sigomei.domain.OrdenMantenimiento;
import mx.uv.sigomei.domain.Tecnico;
import mx.uv.sigomei.enums.EstadoOrden;

public interface OrdenService {
    OrdenMantenimiento crear(OrdenMantenimiento orden);

    OrdenMantenimiento asignarTecnico(Long idOrden, Tecnico tecnico);

    OrdenMantenimiento cambiarEstado(Long idOrden, EstadoOrden nuevoEstado, LocalDate fechaInicio, LocalDate fechaCierre, BigDecimal costoReal);

    OrdenMantenimiento cancelar(Long idOrden);

    Optional<OrdenMantenimiento> obtenerPorId(Long id);

    List<OrdenMantenimiento> consultarHistorial();
}
