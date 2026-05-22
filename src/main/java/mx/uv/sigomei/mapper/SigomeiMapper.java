package mx.uv.sigomei.mapper;

import mx.uv.sigomei.domain.Equipo;
import mx.uv.sigomei.domain.OrdenMantenimiento;
import mx.uv.sigomei.domain.Tecnico;
import mx.uv.sigomei.dto.EquipoDTO;
import mx.uv.sigomei.dto.OrdenMantenimientoDTO;
import mx.uv.sigomei.dto.TecnicoDTO;

public final class SigomeiMapper {
    private SigomeiMapper() {
    }

    public static Equipo toEquipo(EquipoDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Equipo(dto.getIdEquipo(), dto.getNombre(), dto.getTipo(), dto.getMarca(), dto.getModelo(),
                dto.getNumeroSerie(), dto.getUbicacionPlanta(), dto.getFechaInstalacion(), dto.getEstadoOperativo(),
                dto.getCriticidad());
    }

    public static EquipoDTO toEquipoDTO(Equipo equipo) {
        if (equipo == null) {
            return null;
        }
        return new EquipoDTO(equipo.getId(), equipo.getNombre(), equipo.getTipo(), equipo.getMarca(),
                equipo.getModelo(), equipo.getNumeroSerie(), equipo.getUbicacionPlanta(), equipo.getFechaInstalacion(),
                equipo.getEstadoOperativo(), equipo.getCriticidad());
    }

    public static Tecnico toTecnico(TecnicoDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Tecnico(dto.getIdTecnico(), dto.getNombreCompleto(), dto.getRfc(), dto.getTelefono(),
                dto.getCorreo(), dto.getEspecialidad(), dto.getNivelCertificacion(), dto.getFechaIngreso(),
                dto.getEstatus());
    }

    public static TecnicoDTO toTecnicoDTO(Tecnico tecnico) {
        if (tecnico == null) {
            return null;
        }
        return new TecnicoDTO(tecnico.getId(), tecnico.getNombreCompleto(), tecnico.getRfc(), tecnico.getTelefono(),
                tecnico.getCorreo(), tecnico.getEspecialidad(), tecnico.getNivelCertificacion(),
                tecnico.getFechaIngreso(), tecnico.getEstatus());
    }

    public static OrdenMantenimientoDTO toOrdenDTO(OrdenMantenimiento orden) {
        if (orden == null) {
            return null;
        }
        Long idEquipo = orden.getEquipo() == null ? null : orden.getEquipo().getId();
        Long idTecnico = orden.getTecnico() == null ? null : orden.getTecnico().getId();
        return new OrdenMantenimientoDTO(orden.getId(), idEquipo, idTecnico, orden.getTipoMantenimiento(),
                orden.getFechaProgramada(), orden.getFechaInicio(), orden.getFechaCierre(),
                orden.getDescripcionTrabajo(), orden.getCostoEstimado(), orden.getCostoReal(), orden.getEstadoOrden());
    }

    public static OrdenMantenimiento toOrden(OrdenMantenimientoDTO dto, Equipo equipo, Tecnico tecnico) {
        if (dto == null) {
            return null;
        }
        return new OrdenMantenimiento(dto.getIdOrden(), equipo, tecnico, dto.getTipoMantenimiento(),
                dto.getFechaProgramada(), dto.getFechaInicio(), dto.getFechaCierre(), dto.getDescripcionTrabajo(),
                dto.getCostoEstimado(), dto.getCostoReal(), dto.getEstadoOrden());
    }
}
