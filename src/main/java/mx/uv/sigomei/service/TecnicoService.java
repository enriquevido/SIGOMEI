package mx.uv.sigomei.service;

import mx.uv.sigomei.domain.Tecnico;

import java.util.List;
import java.util.Optional;

public interface TecnicoService {
    Tecnico registrar(Tecnico tecnico);

    Optional<Tecnico> obtenerPorId(Long id);

    List<Tecnico> buscarTodos();

    Tecnico modificar(Tecnico tecnico);

    void eliminar(Long id);
}
