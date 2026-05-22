package mx.uv.sigomei.app;

import java.math.BigDecimal;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.util.Properties;

import mx.uv.sigomei.dto.EquipoDTO;
import mx.uv.sigomei.dto.OrdenMantenimientoDTO;
import mx.uv.sigomei.dto.TecnicoDTO;
import mx.uv.sigomei.enums.Criticidad;
import mx.uv.sigomei.enums.EstadoOrden;
import mx.uv.sigomei.enums.EstatusTecnico;
import mx.uv.sigomei.enums.NivelCertificacion;
import mx.uv.sigomei.enums.TipoEquipo;
import mx.uv.sigomei.remote.IEquipoService;
import mx.uv.sigomei.remote.IOrdenService;
import mx.uv.sigomei.remote.ITecnicoService;

public class ClienteSigomeiDemoApp {

    public static void main(String[] args) throws Exception {
        String configPath = System.getProperty("sigomei.client.config", "config/client.properties");

        Properties properties = PropertiesLoader.load(configPath);

        String host = PropertiesLoader.getString(properties, "rmi.host", "localhost");
        int port = PropertiesLoader.getInt(properties, "rmi.registry.port", 1099);

        String equipoName = PropertiesLoader.getString(properties, "rmi.service.equipo", "EquipoService");
        String tecnicoName = PropertiesLoader.getString(properties, "rmi.service.tecnico", "TecnicoService");
        String ordenName = PropertiesLoader.getString(properties, "rmi.service.orden", "OrdenService");

        Registry registry = LocateRegistry.getRegistry(host, port);

        IEquipoService equipoService = (IEquipoService) registry.lookup(equipoName);
        ITecnicoService tecnicoService = (ITecnicoService) registry.lookup(tecnicoName);
        IOrdenService ordenService = (IOrdenService) registry.lookup(ordenName);

        String sufijo = String.valueOf(System.currentTimeMillis()).substring(7);

        String numeroSerieDemo = "SN-DEMO-" + sufijo;
        String rfcDemo = "DEMO800101" + sufijo;
        String correoDemo = "demo" + sufijo + "@empresa.mx";

        EquipoDTO equipo = equipoService.registrarEquipo(new EquipoDTO(
                null,
                "Compresor Demo " + sufijo,
                TipoEquipo.MECANICO,
                "Atlas Copco",
                "GA55",
                numeroSerieDemo,
                "Planta Norte",
                LocalDate.of(2022, 3, 15),
                "Operativo",
                Criticidad.ALTA
        ));

        System.out.println("Equipo registrado: " + equipo);

        TecnicoDTO tecnicoValido = tecnicoService.registrarTecnico(new TecnicoDTO(
                null,
                "Tecnico Demo " + sufijo,
                rfcDemo,
                "9211234567",
                correoDemo,
                TipoEquipo.MECANICO,
                NivelCertificacion.III,
                LocalDate.of(2020, 1, 10),
                EstatusTecnico.ACTIVO
        ));

        System.out.println("Tecnico registrado: " + tecnicoValido);

        OrdenMantenimientoDTO orden = ordenService.crearOrden(new OrdenMantenimientoDTO(
                null,
                equipo.getIdEquipo(),
                tecnicoValido.getIdTecnico(),
                "Preventivo",
                LocalDate.of(2026, 6, 1),
                null,
                null,
                "Cambio de aceite",
                BigDecimal.valueOf(3500),
                null,
                EstadoOrden.PROGRAMADA
        ));

        System.out.println("Orden creada correctamente: " + orden);

        try {
            ordenService.crearOrden(new OrdenMantenimientoDTO(
                    null,
                    equipo.getIdEquipo(),
                    tecnicoValido.getIdTecnico(),
                    "Preventivo",
                    LocalDate.of(2026, 6, 1),
                    null,
                    null,
                    "Orden duplicada",
                    BigDecimal.valueOf(2000),
                    null,
                    EstadoOrden.PROGRAMADA
            ));
        } catch (RuntimeException e) {
            System.out.println("Regla rechazada correctamente: " + e.getMessage());
        }

        OrdenMantenimientoDTO ordenEnEjecucion = ordenService.actualizarEstadoOrden(
                orden.getIdOrden(),
                EstadoOrden.EN_EJECUCION,
                LocalDate.of(2026, 6, 1),
                null,
                null
        );

        System.out.println("Orden actualizada a EN_EJECUCION: " + ordenEnEjecucion);
    }
}