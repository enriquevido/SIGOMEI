package mx.uv.sigomei.service.impl;

import mx.uv.sigomei.domain.OrdenMantenimiento;
import mx.uv.sigomei.domain.Tecnico;
import mx.uv.sigomei.enums.EstadoOrden;
import mx.uv.sigomei.repository.OrdenRepository;
import mx.uv.sigomei.service.OrdenService;
import mx.uv.sigomei.state.GestorEstadoOrden;
import mx.uv.sigomei.validator.ValidadorReglas;

import java.util.List;
import java.util.Optional;

public class OrdenServiceImpl implements OrdenService {
    private final OrdenRepository ordenRepository;
    private final ValidadorReglas validadorReglas;
    private final GestorEstadoOrden gestorEstadoOrden;

    public OrdenServiceImpl(OrdenRepository ordenRepository, ValidadorReglas validadorReglas,
                            GestorEstadoOrden gestorEstadoOrden) {
        this.ordenRepository = ordenRepository;
        this.validadorReglas = validadorReglas;
        this.gestorEstadoOrden = gestorEstadoOrden;
    }

    @Override
    public OrdenMantenimiento crear(OrdenMantenimiento orden) {
        throw new UnsupportedOperationException("Pendiente fase VERDE TDD");
    }

    @Override
    public OrdenMantenimiento asignarTecnico(Long idOrden, Tecnico tecnico) {
        throw new UnsupportedOperationException("Pendiente fase VERDE TDD");
    }

    @Override
    public OrdenMantenimiento cambiarEstado(Long idOrden, EstadoOrden nuevoEstado) {
        throw new UnsupportedOperationException("Pendiente fase VERDE TDD");
    }

    @Override
    public OrdenMantenimiento cancelar(Long idOrden) {
        throw new UnsupportedOperationException("Pendiente fase VERDE TDD");
    }

    @Override
    public Optional<OrdenMantenimiento> obtenerPorId(Long id) {
        throw new UnsupportedOperationException("Pendiente fase VERDE TDD");
    }

    @Override
    public List<OrdenMantenimiento> consultarHistorial() {
        throw new UnsupportedOperationException("Pendiente fase VERDE TDD");
    }
}
