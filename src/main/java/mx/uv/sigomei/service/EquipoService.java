package mx.uv.sigomei.service;

import mx.uv.sigomei.domain.Equipo;

import java.util.List;
import java.util.Optional;

public interface EquipoService {
    Equipo registrar(Equipo equipo);

    Optional<Equipo> obtenerPorId(Long id);

    List<Equipo> buscarTodos();

    Equipo modificar(Equipo equipo);

    void eliminar(Long id);
}
