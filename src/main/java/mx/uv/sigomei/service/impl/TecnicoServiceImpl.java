package mx.uv.sigomei.service.impl;

import mx.uv.sigomei.domain.Tecnico;
import mx.uv.sigomei.repository.OrdenRepository;
import mx.uv.sigomei.repository.TecnicoRepository;
import mx.uv.sigomei.service.TecnicoService;
import mx.uv.sigomei.validator.ValidadorReglas;

import java.util.List;
import java.util.Optional;

public class TecnicoServiceImpl implements TecnicoService {
    private final TecnicoRepository tecnicoRepository;
    private final OrdenRepository ordenRepository;
    private final ValidadorReglas validadorReglas;

    public TecnicoServiceImpl(TecnicoRepository tecnicoRepository, OrdenRepository ordenRepository,
                              ValidadorReglas validadorReglas) {
        this.tecnicoRepository = tecnicoRepository;
        this.ordenRepository = ordenRepository;
        this.validadorReglas = validadorReglas;
    }

    @Override
    public Tecnico registrar(Tecnico tecnico) {
        throw new UnsupportedOperationException("Pendiente fase VERDE TDD");
    }

    @Override
    public Optional<Tecnico> obtenerPorId(Long id) {
        throw new UnsupportedOperationException("Pendiente fase VERDE TDD");
    }

    @Override
    public List<Tecnico> buscarTodos() {
        throw new UnsupportedOperationException("Pendiente fase VERDE TDD");
    }

    @Override
    public Tecnico modificar(Tecnico tecnico) {
        throw new UnsupportedOperationException("Pendiente fase VERDE TDD");
    }

    @Override
    public void eliminar(Long id) {
        throw new UnsupportedOperationException("Pendiente fase VERDE TDD");
    }
}
