package mx.uv.sigomei.repository.memory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import mx.uv.sigomei.domain.Equipo;
import mx.uv.sigomei.domain.OrdenMantenimiento;
import mx.uv.sigomei.domain.Tecnico;
import mx.uv.sigomei.repository.OrdenRepository;

public class InMemoryOrdenRepository implements OrdenRepository {
    private final Map<Long, OrdenMantenimiento> datos = new LinkedHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(1);

    @Override
    public synchronized Optional<OrdenMantenimiento> findById(Long id) {
        return Optional.ofNullable(datos.get(id));
    }

    @Override
    public synchronized OrdenMantenimiento save(OrdenMantenimiento orden) {
        if (orden.getId() == null) {
            orden.setId(secuencia.getAndIncrement());
        }
        datos.put(orden.getId(), orden);
        return orden;
    }

    @Override
    public synchronized List<OrdenMantenimiento> findByEquipo(Equipo equipo) {
        return datos.values().stream()
                .filter(orden -> mismoId(equipo == null ? null : equipo.getId(),
                        orden.getEquipo() == null ? null : orden.getEquipo().getId()))
                .toList();
    }

    @Override
    public synchronized List<OrdenMantenimiento> findByTecnico(Tecnico tecnico) {
        return datos.values().stream()
                .filter(orden -> mismoId(tecnico == null ? null : tecnico.getId(),
                        orden.getTecnico() == null ? null : orden.getTecnico().getId()))
                .toList();
    }

    @Override
    public synchronized List<OrdenMantenimiento> findAll() {
        return new ArrayList<>(datos.values());
    }

    private boolean mismoId(Long a, Long b) {
        return a != null && a.equals(b);
    }
}
