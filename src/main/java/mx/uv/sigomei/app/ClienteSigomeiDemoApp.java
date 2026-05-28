package mx.uv.sigomei.app;

import java.math.BigDecimal;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

import mx.uv.sigomei.dto.EquipoDTO;
import mx.uv.sigomei.dto.FiltroOrdenDTO;
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

        // Sufijo único por ejecución para evitar colisiones en RFC, número de serie y correo
        String sfx = String.valueOf(System.currentTimeMillis()).substring(7);

        // =========================================================
        // CRUD TÉCNICO
        // =========================================================
        System.out.println("\n===== CRUD TÉCNICO =====");

        // CREATE
        TecnicoDTO tecnico = tecnicoService.registrarTecnico(new TecnicoDTO(
                null,
                "Carlos Martínez " + sfx,
                "MARC800101" + sfx.substring(0, 3),
                "9211234567",
                "cmartinez" + sfx + "@empresa.mx",
                TipoEquipo.MECANICO,
                NivelCertificacion.III,
                LocalDate.of(2020, 1, 10),
                EstatusTecnico.ACTIVO
        ));
        System.out.println("[CREATE]   " + tecnico);

        // READ por id
        TecnicoDTO tecnicoLeido = tecnicoService.obtenerTecnicoPorId(tecnico.getIdTecnico());
        System.out.println("[READ]     " + tecnicoLeido);

        // READ todos
        List<TecnicoDTO> todosTecnicos = tecnicoService.buscarTecnicos();
        System.out.println("[READ ALL] Total técnicos en sistema: " + todosTecnicos.size());

        // UPDATE — cambiar teléfono y correo
        tecnico.setTelefono("9217654321");
        tecnico.setCorreo("cmartinez.upd" + sfx + "@empresa.mx");
        TecnicoDTO tecnicoActualizado = tecnicoService.modificarTecnico(tecnico);
        System.out.println("[UPDATE]   " + tecnicoActualizado);

        // DELETE — técnico sin órdenes para poder eliminarlo
        TecnicoDTO tecnicoParaEliminar = tecnicoService.registrarTecnico(new TecnicoDTO(
                null,
                "Técnico Borrar " + sfx,
                "BORR800101" + sfx.substring(0, 3),
                "9219999888",
                "tborrar" + sfx + "@empresa.mx",
                TipoEquipo.ELECTRICO,
                NivelCertificacion.I,
                LocalDate.of(2023, 6, 1),
                EstatusTecnico.ACTIVO
        ));
        tecnicoService.eliminarTecnico(tecnicoParaEliminar.getIdTecnico());
        System.out.println("[DELETE]   Técnico id=" + tecnicoParaEliminar.getIdTecnico() + " eliminado");

        // =========================================================
        // CRUD EQUIPO
        // =========================================================
        System.out.println("\n===== CRUD EQUIPO =====");

        // CREATE
        EquipoDTO equipo = equipoService.registrarEquipo(new EquipoDTO(
                null,
                "Compresor Atlas " + sfx,
                TipoEquipo.MECANICO,
                "Atlas Copco",
                "GA55",
                "SN-ATL-" + sfx,
                "Planta Norte",
                LocalDate.of(2022, 3, 15),
                "Operativo",
                Criticidad.ALTA
        ));
        System.out.println("[CREATE]   " + equipo);

        // READ por id
        EquipoDTO equipoLeido = equipoService.obtenerEquipoPorId(equipo.getIdEquipo());
        System.out.println("[READ]     " + equipoLeido);

        // READ todos
        List<EquipoDTO> todosEquipos = equipoService.buscarEquipos();
        System.out.println("[READ ALL] Total equipos en sistema: " + todosEquipos.size());

        // UPDATE — cambiar ubicación y estado operativo
        equipo.setUbicacionPlanta("Planta Sur");
        equipo.setEstadoOperativo("En mantenimiento");
        EquipoDTO equipoActualizado = equipoService.modificarEquipo(equipo);
        System.out.println("[UPDATE]   " + equipoActualizado);

        // DELETE — equipo sin órdenes para poder eliminarlo
        EquipoDTO equipoParaEliminar = equipoService.registrarEquipo(new EquipoDTO(
                null,
                "Equipo Borrar " + sfx,
                TipoEquipo.ELECTRICO,
                "Siemens",
                "S7-300",
                "SN-BRR-" + sfx,
                "Almacén",
                LocalDate.of(2021, 1, 1),
                "Fuera de servicio",
                Criticidad.BAJA
        ));
        equipoService.eliminarEquipo(equipoParaEliminar.getIdEquipo());
        System.out.println("[DELETE]   Equipo id=" + equipoParaEliminar.getIdEquipo() + " eliminado");

        // =========================================================
        // CRUD ORDEN DE MANTENIMIENTO
        // =========================================================
        System.out.println("\n===== CRUD ORDEN DE MANTENIMIENTO =====");

        LocalDate fechaOrden = LocalDate.of(2026, 8, 15);

        // CREATE
        OrdenMantenimientoDTO orden = ordenService.crearOrden(new OrdenMantenimientoDTO(
                null,
                equipo.getIdEquipo(),
                tecnico.getIdTecnico(),
                "Preventivo",
                fechaOrden,
                null, null,
                "Cambio de aceite y filtros",
                BigDecimal.valueOf(3500),
                null,
                EstadoOrden.PROGRAMADA
        ));
        System.out.println("[CREATE]   " + orden);

        // READ por id
        OrdenMantenimientoDTO ordenLeida = ordenService.obtenerOrdenPorId(orden.getIdOrden());
        System.out.println("[READ]     " + ordenLeida);

        // READ todos — historial sin filtro
        List<OrdenMantenimientoDTO> historial = ordenService.consultarHistorial(new FiltroOrdenDTO());
        System.out.println("[READ ALL] Total órdenes en historial: " + historial.size());

        // UPDATE — reasignar técnico (solo válido en estado PROGRAMADA)
        TecnicoDTO tecnicoReasignado = tecnicoService.registrarTecnico(new TecnicoDTO(
                null,
                "Ana López " + sfx,
                "LOPA800101" + sfx.substring(0, 3),
                "9213456789",
                "alopez" + sfx + "@empresa.mx",
                TipoEquipo.MECANICO,
                NivelCertificacion.II,
                LocalDate.of(2019, 5, 20),
                EstatusTecnico.ACTIVO
        ));
        OrdenMantenimientoDTO ordenReasignada = ordenService.asignarTecnico(
                orden.getIdOrden(), tecnicoReasignado.getIdTecnico()
        );
        System.out.println("[UPDATE asignarTecnico] " + ordenReasignada);

        // UPDATE — PROGRAMADA → EN_EJECUCION
        OrdenMantenimientoDTO ordenEnEjecucion = ordenService.actualizarEstadoOrden(
                orden.getIdOrden(),
                EstadoOrden.EN_EJECUCION,
                fechaOrden,
                null,
                null
        );
        System.out.println("[UPDATE EN_EJECUCION]   " + ordenEnEjecucion);

        // UPDATE — EN_EJECUCION → FINALIZADA (requiere fechaCierre y costoReal)
        OrdenMantenimientoDTO ordenFinalizada = ordenService.actualizarEstadoOrden(
                orden.getIdOrden(),
                EstadoOrden.FINALIZADA,
                fechaOrden,
                LocalDate.of(2026, 8, 16),
                BigDecimal.valueOf(3200)
        );
        System.out.println("[UPDATE FINALIZADA]     " + ordenFinalizada);

        // CANCELAR — crear orden separada y cancelarla (equivale a DELETE)
        OrdenMantenimientoDTO ordenParaCancelar = ordenService.crearOrden(new OrdenMantenimientoDTO(
                null,
                equipo.getIdEquipo(),
                tecnico.getIdTecnico(),
                "Correctivo",
                LocalDate.of(2026, 9, 10),
                null, null,
                "Reparación de fuga",
                BigDecimal.valueOf(8000),
                null,
                EstadoOrden.PROGRAMADA
        ));
        OrdenMantenimientoDTO ordenCancelada = ordenService.cancelarOrden(ordenParaCancelar.getIdOrden());
        System.out.println("[CANCELAR]              " + ordenCancelada);

        // =========================================================
        // REGLAS DE NEGOCIO
        // =========================================================
        System.out.println("\n===== REGLAS DE NEGOCIO =====");

        // --- RN-02: no puede haber dos órdenes activas para el mismo equipo en la misma fecha ---
        System.out.println("\n-- RN-02: Orden activa duplicada (mismo equipo, misma fecha) --");
        OrdenMantenimientoDTO ordenBase = ordenService.crearOrden(new OrdenMantenimientoDTO(
                null,
                equipo.getIdEquipo(),
                tecnico.getIdTecnico(),
                "Preventivo",
                LocalDate.of(2026, 10, 1),
                null, null,
                "Revisión general",
                BigDecimal.valueOf(1000),
                null,
                EstadoOrden.PROGRAMADA
        ));
        System.out.println("  Orden base creada id=" + ordenBase.getIdOrden()
                + " (equipo=" + equipo.getIdEquipo() + ", fecha=2026-10-01)");
        try {
            ordenService.crearOrden(new OrdenMantenimientoDTO(
                    null,
                    equipo.getIdEquipo(),
                    tecnico.getIdTecnico(),
                    "Correctivo",
                    LocalDate.of(2026, 10, 1),
                    null, null,
                    "Intento de orden duplicada",
                    BigDecimal.valueOf(500),
                    null,
                    EstadoOrden.PROGRAMADA
            ));
            System.out.println("  [FALLO] La orden duplicada no fue rechazada");
        } catch (RuntimeException e) {
            System.out.println("  [RN-02 OK] Rechazada correctamente: " + e.getMessage());
        }

        // --- RN-07: equipo de criticidad ALTA requiere técnico con certificación II o III ---
        System.out.println("\n-- RN-07: Criticidad ALTA requiere certificación nivel II o III --");
        TecnicoDTO tecnicoNivel1 = tecnicoService.registrarTecnico(new TecnicoDTO(
                null,
                "Junior Mecánico " + sfx,
                "JUNM800101" + sfx.substring(0, 3),
                "9210001111",
                "jmec" + sfx + "@empresa.mx",
                TipoEquipo.MECANICO,
                NivelCertificacion.I,
                LocalDate.of(2024, 1, 15),
                EstatusTecnico.ACTIVO
        ));
        System.out.println("  Técnico nivel I registrado id=" + tecnicoNivel1.getIdTecnico()
                + " (especialidad=MECANICO, certificacion=I)");
        System.out.println("  Equipo con criticidad=" + equipo.getCriticidad()
                + " id=" + equipo.getIdEquipo());
        try {
            ordenService.crearOrden(new OrdenMantenimientoDTO(
                    null,
                    equipo.getIdEquipo(),
                    tecnicoNivel1.getIdTecnico(),
                    "Preventivo",
                    LocalDate.of(2026, 11, 5),
                    null, null,
                    "Intento con técnico nivel I en equipo criticidad ALTA",
                    BigDecimal.valueOf(1500),
                    null,
                    EstadoOrden.PROGRAMADA
            ));
            System.out.println("  [FALLO] La orden no fue rechazada por RN-07");
        } catch (RuntimeException e) {
            System.out.println("  [RN-07 OK] Rechazada correctamente: " + e.getMessage());
        }
    }
}
