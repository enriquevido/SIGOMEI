package mx.uv.sigomei.service;

import mx.uv.sigomei.domain.Equipo;
import mx.uv.sigomei.enums.Criticidad;
import mx.uv.sigomei.enums.TipoEquipo;

import java.util.List;
import java.util.Optional;

public interface EquipoService {
    Equipo registrar(Equipo equipo);

    Optional<Equipo> obtenerPorId(Long id);

    List<Equipo> buscarTodos();

    List<Equipo> buscarConFiltros(String nombre, TipoEquipo tipo, String numeroSerie,
                                  String ubicacion, String estadoOperativo, Criticidad criticidad);

    Equipo modificar(Equipo equipo);

    void eliminar(Long id);
}
