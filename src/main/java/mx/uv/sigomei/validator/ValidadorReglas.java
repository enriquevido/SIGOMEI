package mx.uv.sigomei.validator;

import mx.uv.sigomei.domain.Equipo;
import mx.uv.sigomei.domain.OrdenMantenimiento;
import mx.uv.sigomei.domain.Tecnico;

import java.time.LocalDate;
import java.util.List;

public interface ValidadorReglas {
    void validarEspecialidad(Equipo equipo, Tecnico tecnico);

    void validarOrdenActivaDuplicada(Equipo equipo, LocalDate fecha, List<OrdenMantenimiento> existentes);

    void validarTecnicoActivo(Tecnico tecnico);

    void validarEliminacionSinOrdenes(List<OrdenMantenimiento> ordenes);

    void validarFechas(OrdenMantenimiento orden);

    void validarDatosFinalizacion(OrdenMantenimiento orden);

    void validarCriticidadAlta(Equipo equipo, Tecnico tecnico);
}
