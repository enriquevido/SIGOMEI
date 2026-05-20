package mx.uv.sigomei.validator;

import mx.uv.sigomei.domain.Equipo;
import mx.uv.sigomei.domain.OrdenMantenimiento;
import mx.uv.sigomei.domain.Tecnico;

import java.time.LocalDate;
import java.util.List;

public class ValidadorReglasImpl implements ValidadorReglas {
    @Override
    public void validarEspecialidad(Equipo equipo, Tecnico tecnico) {
        throw new UnsupportedOperationException("Pendiente fase VERDE TDD");
    }

    @Override
    public void validarOrdenActivaDuplicada(Equipo equipo, LocalDate fecha, List<OrdenMantenimiento> existentes) {
        throw new UnsupportedOperationException("Pendiente fase VERDE TDD");
    }

    @Override
    public void validarTecnicoActivo(Tecnico tecnico) {
        throw new UnsupportedOperationException("Pendiente fase VERDE TDD");
    }

    @Override
    public void validarEliminacionSinOrdenes(List<OrdenMantenimiento> ordenes) {
        throw new UnsupportedOperationException("Pendiente fase VERDE TDD");
    }

    @Override
    public void validarFechas(OrdenMantenimiento orden) {
        throw new UnsupportedOperationException("Pendiente fase VERDE TDD");
    }

    @Override
    public void validarDatosFinalizacion(OrdenMantenimiento orden) {
        throw new UnsupportedOperationException("Pendiente fase VERDE TDD");
    }

    @Override
    public void validarCriticidadAlta(Equipo equipo, Tecnico tecnico) {
        throw new UnsupportedOperationException("Pendiente fase VERDE TDD");
    }
}
