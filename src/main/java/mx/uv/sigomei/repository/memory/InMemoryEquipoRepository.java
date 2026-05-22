package mx.uv.sigomei.repository.memory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import mx.uv.sigomei.domain.Equipo;
import mx.uv.sigomei.repository.EquipoRepository;

public class InMemoryEquipoRepository implements EquipoRepository {
    private final Map<Long, Equipo> datos = new LinkedHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(1);

    @Override
    public synchronized Optional<Equipo> findById(Long id) {
        return Optional.ofNullable(datos.get(id));
    }

    @Override
    public synchronized Equipo save(Equipo equipo) {
        if (equipo.getId() == null) {
            equipo.setId(secuencia.getAndIncrement());
        }
        datos.put(equipo.getId(), equipo);
        return equipo;
    }

    @Override
    public synchronized void deleteById(Long id) {
        datos.remove(id);
    }

    @Override
    public synchronized List<Equipo> findAll() {
        return new ArrayList<>(datos.values());
    }
}
