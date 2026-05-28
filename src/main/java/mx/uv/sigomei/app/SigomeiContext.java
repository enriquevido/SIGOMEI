package mx.uv.sigomei.app;

import mx.uv.sigomei.repository.EquipoRepository;
import mx.uv.sigomei.repository.OrdenRepository;
import mx.uv.sigomei.repository.TecnicoRepository;
import mx.uv.sigomei.repository.jdbc.DatabaseConnectionProvider;
import mx.uv.sigomei.repository.jdbc.JdbcEquipoRepository;
import mx.uv.sigomei.repository.jdbc.JdbcOrdenRepository;
import mx.uv.sigomei.repository.jdbc.JdbcTecnicoRepository;
import mx.uv.sigomei.repository.jdbc.JdbcUsuarioRepository;
import mx.uv.sigomei.service.AuthService;
import mx.uv.sigomei.service.EquipoService;
import mx.uv.sigomei.service.OrdenService;
import mx.uv.sigomei.service.TecnicoService;
import mx.uv.sigomei.service.impl.AuthServiceImpl;
import mx.uv.sigomei.service.impl.EquipoServiceImpl;
import mx.uv.sigomei.service.impl.OrdenServiceImpl;
import mx.uv.sigomei.service.impl.TecnicoServiceImpl;
import mx.uv.sigomei.state.GestorEstadoOrden;
import mx.uv.sigomei.state.GestorEstadoOrdenImpl;
import mx.uv.sigomei.validator.ValidadorReglas;
import mx.uv.sigomei.validator.ValidadorReglasImpl;

public class SigomeiContext {

    private final DatabaseConnectionProvider connectionProvider =
            new DatabaseConnectionProvider(System.getProperty("sigomei.config", "config/server.properties"));

    private final EquipoRepository equipoRepository =
            new JdbcEquipoRepository(connectionProvider);

    private final TecnicoRepository tecnicoRepository =
            new JdbcTecnicoRepository(connectionProvider);

    private final OrdenRepository ordenRepository =
            new JdbcOrdenRepository(connectionProvider);

    private final JdbcUsuarioRepository usuarioRepository =
            new JdbcUsuarioRepository(connectionProvider);

    private final ValidadorReglas validadorReglas = new ValidadorReglasImpl();

    private final GestorEstadoOrden gestorEstadoOrden = new GestorEstadoOrdenImpl();

    private final EquipoService equipoService =
            new EquipoServiceImpl(equipoRepository, ordenRepository, validadorReglas);

    private final TecnicoService tecnicoService =
            new TecnicoServiceImpl(tecnicoRepository, ordenRepository, validadorReglas);

    private final OrdenService ordenService =
            new OrdenServiceImpl(ordenRepository, validadorReglas, gestorEstadoOrden);

    private final AuthService authService =
            new AuthServiceImpl(usuarioRepository);

    public EquipoService equipoService() {
        return equipoService;
    }

    public TecnicoService tecnicoService() {
        return tecnicoService;
    }

    public OrdenService ordenService() {
        return ordenService;
    }

    public AuthService authService() {
        return authService;
    }

    public void cerrar() {
        connectionProvider.close();
    }
}
