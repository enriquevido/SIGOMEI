package mx.uv.sigomei.app;

import mx.uv.sigomei.repository.EquipoRepository;
import mx.uv.sigomei.repository.OrdenRepository;
import mx.uv.sigomei.repository.TecnicoRepository;
import mx.uv.sigomei.repository.memory.InMemoryEquipoRepository;
import mx.uv.sigomei.repository.memory.InMemoryOrdenRepository;
import mx.uv.sigomei.repository.memory.InMemoryTecnicoRepository;
import mx.uv.sigomei.service.EquipoService;
import mx.uv.sigomei.service.OrdenService;
import mx.uv.sigomei.service.TecnicoService;
import mx.uv.sigomei.service.impl.EquipoServiceImpl;
import mx.uv.sigomei.service.impl.OrdenServiceImpl;
import mx.uv.sigomei.service.impl.TecnicoServiceImpl;
import mx.uv.sigomei.state.GestorEstadoOrden;
import mx.uv.sigomei.state.GestorEstadoOrdenImpl;
import mx.uv.sigomei.validator.ValidadorReglas;
import mx.uv.sigomei.validator.ValidadorReglasImpl;

public class SigomeiContext {

    private final EquipoRepository equipoRepository = new InMemoryEquipoRepository();

    private final TecnicoRepository tecnicoRepository = new InMemoryTecnicoRepository();

    private final OrdenRepository ordenRepository = new InMemoryOrdenRepository();

    private final ValidadorReglas validadorReglas = new ValidadorReglasImpl();

    private final GestorEstadoOrden gestorEstadoOrden = new GestorEstadoOrdenImpl();

    private final EquipoService equipoService =
            new EquipoServiceImpl(equipoRepository, ordenRepository, validadorReglas);

    private final TecnicoService tecnicoService =
            new TecnicoServiceImpl(tecnicoRepository, ordenRepository, validadorReglas);

    private final OrdenService ordenService =
            new OrdenServiceImpl(ordenRepository, validadorReglas, gestorEstadoOrden);

    public EquipoService equipoService() {
        return equipoService;
    }

    public TecnicoService tecnicoService() {
        return tecnicoService;
    }

    public OrdenService ordenService() {
        return ordenService;
    }
}