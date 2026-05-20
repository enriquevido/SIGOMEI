package mx.uv.sigomei.repository;

import mx.uv.sigomei.domain.Equipo;

import java.util.List;
import java.util.Optional;

public interface EquipoRepository {
    Optional<Equipo> findById(Long id);

    Equipo save(Equipo equipo);

    void deleteById(Long id);

    List<Equipo> findAll();
}
