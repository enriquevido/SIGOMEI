package mx.uv.sigomei.service.impl;

import mx.uv.sigomei.domain.Equipo;
import mx.uv.sigomei.repository.EquipoRepository;
import mx.uv.sigomei.repository.OrdenRepository;
import mx.uv.sigomei.service.EquipoService;
import mx.uv.sigomei.validator.ValidadorReglas;

import java.util.List;
import java.util.Optional;

public class EquipoServiceImpl implements EquipoService {
    private final EquipoRepository equipoRepository;
    private final OrdenRepository ordenRepository;
    private final ValidadorReglas validadorReglas;

    public EquipoServiceImpl(EquipoRepository equipoRepository, OrdenRepository ordenRepository,
                             ValidadorReglas validadorReglas) {
        this.equipoRepository = equipoRepository;
        this.ordenRepository = ordenRepository;
        this.validadorReglas = validadorReglas;
    }

    @Override
    public Equipo registrar(Equipo equipo) {
        throw new UnsupportedOperationException("Pendiente fase VERDE TDD");
    }

    @Override
    public Optional<Equipo> obtenerPorId(Long id) {
        throw new UnsupportedOperationException("Pendiente fase VERDE TDD");
    }

    @Override
    public List<Equipo> buscarTodos() {
        throw new UnsupportedOperationException("Pendiente fase VERDE TDD");
    }

    @Override
    public Equipo modificar(Equipo equipo) {
        throw new UnsupportedOperationException("Pendiente fase VERDE TDD");
    }

    @Override
    public void eliminar(Long id) {
        throw new UnsupportedOperationException("Pendiente fase VERDE TDD");
    }
}
