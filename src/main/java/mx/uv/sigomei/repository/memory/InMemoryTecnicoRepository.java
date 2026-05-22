package mx.uv.sigomei.repository.memory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import mx.uv.sigomei.domain.Tecnico;
import mx.uv.sigomei.repository.TecnicoRepository;

public class InMemoryTecnicoRepository implements TecnicoRepository {
    private final Map<Long, Tecnico> datos = new LinkedHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(1);

    @Override
    public synchronized Optional<Tecnico> findById(Long id) {
        return Optional.ofNullable(datos.get(id));
    }

    @Override
    public synchronized Tecnico save(Tecnico tecnico) {
        if (tecnico.getId() == null) {
            tecnico.setId(secuencia.getAndIncrement());
        }
        datos.put(tecnico.getId(), tecnico);
        return tecnico;
    }

    @Override
    public synchronized void deleteById(Long id) {
        datos.remove(id);
    }

    @Override
    public synchronized List<Tecnico> findAll() {
        return new ArrayList<>(datos.values());
    }
}
