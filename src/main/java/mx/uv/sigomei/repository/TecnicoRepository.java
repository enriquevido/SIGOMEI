package mx.uv.sigomei.repository;

import mx.uv.sigomei.domain.Tecnico;

import java.util.List;
import java.util.Optional;

public interface TecnicoRepository {
    Optional<Tecnico> findById(Long id);

    Tecnico save(Tecnico tecnico);

    void deleteById(Long id);

    List<Tecnico> findAll();
}
