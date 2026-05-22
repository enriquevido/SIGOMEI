package mx.uv.sigomei.validator;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import mx.uv.sigomei.domain.Equipo;
import mx.uv.sigomei.domain.OrdenMantenimiento;
import mx.uv.sigomei.domain.Tecnico;
import mx.uv.sigomei.enums.Criticidad;
import mx.uv.sigomei.enums.EstadoOrden;
import mx.uv.sigomei.enums.EstatusTecnico;
import mx.uv.sigomei.enums.NivelCertificacion;
import mx.uv.sigomei.exception.ReglaNegocioException;

public class ValidadorReglasImpl implements ValidadorReglas {

    @Override
    public void validarEspecialidad(Equipo equipo, Tecnico tecnico) {
        if (equipo == null || tecnico == null) {
            throw new ReglaNegocioException("RN-01: el equipo y el tecnico son obligatorios");
        }

        if (equipo.getTipo() == null || tecnico.getEspecialidad() == null) {
            throw new ReglaNegocioException("RN-01: el tipo del equipo y la especialidad del tecnico son obligatorios");
        }

        if (equipo.getTipo() != tecnico.getEspecialidad()) {
            throw new ReglaNegocioException("RN-01: la especialidad del tecnico no coincide con el tipo del equipo");
        }
    }

    @Override
    public void validarOrdenActivaDuplicada(Equipo equipo, LocalDate fecha, List<OrdenMantenimiento> existentes) {
        if (equipo == null || equipo.getId() == null || fecha == null) {
            throw new ReglaNegocioException("RN-02: el equipo y la fecha programada son obligatorios");
        }

        if (existentes == null) {
            return;
        }

        boolean existeConflicto = existentes.stream()
                .filter(Objects::nonNull)
                .filter(orden -> orden.getEquipo() != null)
                .filter(orden -> equipo.getId().equals(orden.getEquipo().getId()))
                .filter(orden -> fecha.equals(orden.getFechaProgramada()))
                .anyMatch(orden -> esOrdenActiva(orden.getEstadoOrden()));

        if (existeConflicto) {
            throw new ReglaNegocioException("RN-02: el equipo ya tiene una orden activa en esa fecha");
        }
    }

    @Override
    public void validarTecnicoActivo(Tecnico tecnico) {
        if (tecnico == null || tecnico.getEstatus() == null) {
            throw new ReglaNegocioException("RN-03: el tecnico y su estatus son obligatorios");
        }

        if (tecnico.getEstatus() == EstatusTecnico.INACTIVO) {
            throw new ReglaNegocioException("RN-03: un tecnico inactivo no puede ser asignado");
        }
    }

    @Override
    public void validarEliminacionSinOrdenes(List<OrdenMantenimiento> ordenes) {
        if (ordenes != null && !ordenes.isEmpty()) {
            throw new ReglaNegocioException("RN-04: no se puede eliminar una entidad con ordenes registradas");
        }
    }

    @Override
    public void validarFechas(OrdenMantenimiento orden) {
        if (orden == null) {
            throw new ReglaNegocioException("RN-05: la orden es obligatoria");
        }

        LocalDate fechaProgramada = orden.getFechaProgramada();
        LocalDate fechaInicio = orden.getFechaInicio();
        LocalDate fechaCierre = orden.getFechaCierre();

        if (fechaProgramada == null) {
            throw new ReglaNegocioException("RN-05: la fecha programada es obligatoria");
        }

        if (fechaInicio != null && fechaInicio.isBefore(fechaProgramada)) {
            throw new ReglaNegocioException("RN-05: la fecha de inicio no puede ser anterior a la fecha programada");
        }

        if (fechaCierre != null && fechaInicio == null) {
            throw new ReglaNegocioException("RN-05: la fecha de cierre requiere fecha de inicio");
        }

        if (fechaCierre != null && fechaCierre.isBefore(fechaInicio)) {
            throw new ReglaNegocioException("RN-05: la fecha de cierre no puede ser anterior a la fecha de inicio");
        }
    }

    @Override
    public void validarDatosFinalizacion(OrdenMantenimiento orden) {
        if (orden == null || orden.getEstadoOrden() == null) {
            throw new ReglaNegocioException("RN-06: la orden y su estado son obligatorios");
        }

        boolean tieneDatosCierre = orden.getFechaCierre() != null || orden.getCostoReal() != null;

        if (orden.getEstadoOrden() == EstadoOrden.FINALIZADA) {
            if (orden.getFechaCierre() == null || orden.getCostoReal() == null) {
                throw new ReglaNegocioException("RN-06: una orden finalizada requiere fecha de cierre y costo real");
            }
            return;
        }

        if (tieneDatosCierre) {
            throw new ReglaNegocioException("RN-06: solo una orden finalizada puede conservar datos de cierre definitivo");
        }
    }

    @Override
    public void validarCriticidadAlta(Equipo equipo, Tecnico tecnico) {
        if (equipo == null || tecnico == null) {
            throw new ReglaNegocioException("RN-07: el equipo y el tecnico son obligatorios");
        }

        if (equipo.getCriticidad() == Criticidad.ALTA
                && tecnico.getNivelCertificacion() == NivelCertificacion.I) {
            throw new ReglaNegocioException("RN-07: un equipo de criticidad alta requiere certificacion II o III");
        }
    }

    private boolean esOrdenActiva(EstadoOrden estadoOrden) {
        return estadoOrden == EstadoOrden.PROGRAMADA || estadoOrden == EstadoOrden.EN_EJECUCION;
    }
}