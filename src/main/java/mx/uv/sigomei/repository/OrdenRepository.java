package mx.uv.sigomei.repository;

import mx.uv.sigomei.domain.Equipo;
import mx.uv.sigomei.domain.OrdenMantenimiento;
import mx.uv.sigomei.domain.Tecnico;

import java.util.List;
import java.util.Optional;

public interface OrdenRepository {
    Optional<OrdenMantenimiento> findById(Long id);

    OrdenMantenimiento save(OrdenMantenimiento orden);

    List<OrdenMantenimiento> findByEquipo(Equipo equipo);

    List<OrdenMantenimiento> findByTecnico(Tecnico tecnico);

    List<OrdenMantenimiento> findAll();
}
