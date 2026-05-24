package mx.uv.sigomei.app;

import java.math.BigDecimal;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.util.List;
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

public class ClientePruebasSistemaApp {

    private final IEquipoService equipoService;
    private final ITecnicoService tecnicoService;
    private final IOrdenService ordenService;

    public ClientePruebasSistemaApp() throws Exception {
        String configPath = System.getProperty("sigomei.client.config", "config/client.properties");

        Properties properties = PropertiesLoader.load(configPath);

        String host = PropertiesLoader.getString(properties, "rmi.host", "127.0.0.1");
        int port = PropertiesLoader.getInt(properties, "rmi.registry.port", 1099);

        String equipoName = PropertiesLoader.getString(properties, "rmi.service.equipo", "EquipoService");
        String tecnicoName = PropertiesLoader.getString(properties, "rmi.service.tecnico", "TecnicoService");
        String ordenName = PropertiesLoader.getString(properties, "rmi.service.orden", "OrdenService");

        Registry registry = LocateRegistry.getRegistry(host, port);

        this.equipoService = (IEquipoService) registry.lookup(equipoName);
        this.tecnicoService = (ITecnicoService) registry.lookup(tecnicoName);
        this.ordenService = (IOrdenService) registry.lookup(ordenName);
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Indica el caso a ejecutar. Ejemplo: CP03");
            return;
        }

        ClientePruebasSistemaApp app = new ClientePruebasSistemaApp();

        switch (args[0].toUpperCase()) {
            case "CP03" -> app.cp03ModificarCriticidadEquipo();
            case "CP07" -> app.cp07FinalizarOrden();
            case "CP08" -> app.cp08CancelarOrdenProgramada();
            case "CP10" -> app.cp10EliminarTecnicoSinOrdenes();
            case "CP11" -> app.cp11CambiarTecnicoAsignado();
            case "CP14" -> app.cp14EquipoDuplicado();
            case "CP15" -> app.cp15EspecialidadIncorrecta();
            case "CP17" -> app.cp17TecnicoInactivo();
            case "CP18" -> app.cp18EliminarEquipoConOrdenes();
            case "CP19" -> app.cp19FinalizarSinCierre();
            case "CP20" -> app.cp20CertificacionInsuficiente();
            case "CP21" -> app.cp21TransicionInvalida();
            default -> System.out.println("Caso no reconocido: " + args[0]);
        }
    }

    private void cp03ModificarCriticidadEquipo() throws Exception {
        System.out.println("CP-03 — Modificar criticidad de equipo existente");

        EquipoDTO equipo = equipoService.obtenerEquipoPorId(2L);

        System.out.println("Antes de modificar:");
        System.out.println(equipo);

        equipo.setCriticidad(Criticidad.ALTA);

        EquipoDTO actualizado = equipoService.modificarEquipo(equipo);

        System.out.println("Después de modificar:");
        System.out.println(actualizado);
        System.out.println("CP-03 ejecutado. Verificar en MySQL que id_equipo=2 tenga criticidad=ALTA.");
    }

    private void cp07FinalizarOrden() throws Exception {
        System.out.println("CP-07 — Finalizar orden con fecha de cierre y costo real");

        Long idOrden = 5L;

        OrdenMantenimientoDTO actualizada = ordenService.actualizarEstadoOrden(
                idOrden,
                EstadoOrden.FINALIZADA,
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 3),
                BigDecimal.valueOf(3200)
        );

        System.out.println(actualizada);
        System.out.println("CP-07 ejecutado. Verificar estado FINALIZADA, fecha_cierre y costo_real en MySQL.");
    }

    private void cp08CancelarOrdenProgramada() throws Exception {
        System.out.println("CP-08 — Cancelar orden en estado PROGRAMADA");

        Long idOrden = 1L;

        OrdenMantenimientoDTO cancelada = ordenService.cancelarOrden(idOrden);

        System.out.println(cancelada);
        System.out.println("CP-08 ejecutado. Verificar que la orden quedó CANCELADA en MySQL.");
    }

    private void cp10EliminarTecnicoSinOrdenes() throws Exception {
        System.out.println("CP-10 — Eliminar técnico sin órdenes asociadas");

        String sufijo = String.valueOf(System.currentTimeMillis()).substring(7);

        TecnicoDTO tecnico = tecnicoService.registrarTecnico(new TecnicoDTO(
                null,
                "Tecnico Sin Orden " + sufijo,
                "TSOR" + sufijo,
                "9210000000",
                "sinorden" + sufijo + "@empresa.mx",
                TipoEquipo.MECANICO,
                NivelCertificacion.II,
                LocalDate.of(2024, 1, 1),
                EstatusTecnico.ACTIVO
        ));

        System.out.println("Técnico creado:");
        System.out.println(tecnico);

        tecnicoService.eliminarTecnico(tecnico.getIdTecnico());

        System.out.println("Técnico eliminado correctamente: id=" + tecnico.getIdTecnico());
        System.out.println("CP-10 ejecutado. Verificar que ya no existe en MySQL.");
    }

    private void cp11CambiarTecnicoAsignado() throws Exception {
        System.out.println("CP-11 — Cambiar técnico asignado a orden PROGRAMADA");

        String sufijo = String.valueOf(System.currentTimeMillis()).substring(7);
        String homoclave = sufijo.substring(sufijo.length() - 3);

        EquipoDTO equipo = equipoService.registrarEquipo(new EquipoDTO(
                null,
                "Equipo CP11 " + sufijo,
                TipoEquipo.ELECTRICO,
                "Siemens",
                "S7",
                "SN-CP11-" + sufijo,
                "Planta Sur",
                LocalDate.of(2022, 1, 1),
                "Operativo",
                Criticidad.MEDIA
        ));

        TecnicoDTO tecnicoInicial = tecnicoService.registrarTecnico(new TecnicoDTO(
                null,
                "Tecnico Inicial CP11 " + sufijo,
                "INIA800101" + homoclave,
                "9211111111",
                "ini" + sufijo + "@empresa.mx",
                TipoEquipo.ELECTRICO,
                NivelCertificacion.II,
                LocalDate.of(2021, 1, 1),
                EstatusTecnico.ACTIVO
        ));

        TecnicoDTO tecnicoNuevo = tecnicoService.registrarTecnico(new TecnicoDTO(
                null,
                "Tecnico Nuevo CP11 " + sufijo,
                "NEWA800101" + homoclave,
                "9212222222",
                "new" + sufijo + "@empresa.mx",
                TipoEquipo.ELECTRICO,
                NivelCertificacion.III,
                LocalDate.of(2021, 1, 1),
                EstatusTecnico.ACTIVO
        ));

        OrdenMantenimientoDTO orden = ordenService.crearOrden(new OrdenMantenimientoDTO(
                null,
                equipo.getIdEquipo(),
                tecnicoInicial.getIdTecnico(),
                "Preventivo",
                LocalDate.of(2026, 7, 1),
                null,
                null,
                "Orden CP11",
                BigDecimal.valueOf(1500),
                null,
                EstadoOrden.PROGRAMADA
        ));

        System.out.println("Orden antes de cambiar técnico:");
        System.out.println(orden);

        OrdenMantenimientoDTO actualizada = ordenService.asignarTecnico(
                orden.getIdOrden(),
                tecnicoNuevo.getIdTecnico()
        );

        System.out.println("Orden después de cambiar técnico:");
        System.out.println(actualizada);

        System.out.println("CP-11 ejecutado. Verificar id_tecnico nuevo en MySQL.");
    }

    private void cp14EquipoDuplicado() throws Exception {
        System.out.println("CP-14 — Registrar equipo con número de serie duplicado");

        EquipoDTO duplicado = new EquipoDTO(
                null,
                "Equipo Duplicado",
                TipoEquipo.MECANICO,
                "Marca",
                "Modelo",
                "SN-COMP-001",
                "Planta Norte",
                LocalDate.of(2024, 1, 1),
                "Operativo",
                Criticidad.BAJA
        );

        EquipoDTO resultado = equipoService.registrarEquipo(duplicado);

        System.out.println("ERROR: se registró un equipo duplicado, esto NO debía pasar.");
        System.out.println(resultado);
    }

    private void cp15EspecialidadIncorrecta() throws Exception {
        System.out.println("CP-15 — Asignar técnico con especialidad incorrecta");

        String sufijo = String.valueOf(System.currentTimeMillis()).substring(7);
        String homoclave = sufijo.substring(sufijo.length() - 3);

        EquipoDTO equipoElectrico = equipoService.registrarEquipo(new EquipoDTO(
                null,
                "Equipo Electrico CP15 " + sufijo,
                TipoEquipo.ELECTRICO,
                "Siemens",
                "S7",
                "SN-CP15-" + sufijo,
                "Planta Sur",
                LocalDate.of(2024, 1, 1),
                "Operativo",
                Criticidad.MEDIA
        ));

        TecnicoDTO tecnicoMecanico = tecnicoService.registrarTecnico(new TecnicoDTO(
                null,
                "Tecnico Mecanico CP15 " + sufijo,
                "MECA800101" + homoclave,
                "9213333333",
                "mec" + sufijo + "@empresa.mx",
                TipoEquipo.MECANICO,
                NivelCertificacion.III,
                LocalDate.of(2021, 1, 1),
                EstatusTecnico.ACTIVO
        ));

        System.out.println("Equipo usado:");
        System.out.println(equipoElectrico);

        System.out.println("Técnico usado:");
        System.out.println(tecnicoMecanico);

        try {
            OrdenMantenimientoDTO orden = ordenService.crearOrden(new OrdenMantenimientoDTO(
                    null,
                    equipoElectrico.getIdEquipo(),
                    tecnicoMecanico.getIdTecnico(),
                    "Preventivo",
                    LocalDate.of(2026, 7, 2),
                    null,
                    null,
                    "Orden RN01 inválida",
                    BigDecimal.valueOf(2000),
                    null,
                    EstadoOrden.PROGRAMADA
            ));

            System.out.println("FAIL: se creó una orden con especialidad incorrecta, esto NO debía pasar.");
            System.out.println(orden);
        } catch (Exception e) {
            System.out.println("PASS: el sistema rechazó correctamente la especialidad incorrecta.");
            System.out.println("Excepción recibida: " + e.getClass().getSimpleName());
            System.out.println("Mensaje: " + e.getMessage());
        }

        System.out.println("CP-15 ejecutado. Verificar en MySQL que no exista una orden con descripcion_trabajo='Orden RN01 inválida'.");
    }

    private void cp17TecnicoInactivo() throws Exception {
        System.out.println("CP-17 — Asignar técnico INACTIVO a nueva orden");

        String sufijo = String.valueOf(System.currentTimeMillis()).substring(7);
        String homoclave = sufijo.substring(sufijo.length() - 3);

        EquipoDTO equipo = equipoService.registrarEquipo(new EquipoDTO(
                null,
                "Equipo CP17 " + sufijo,
                TipoEquipo.MECANICO,
                "Atlas",
                "GA",
                "SN-CP17-" + sufijo,
                "Planta Norte",
                LocalDate.of(2024, 1, 1),
                "Operativo",
                Criticidad.MEDIA
        ));

        TecnicoDTO inactivo = tecnicoService.registrarTecnico(new TecnicoDTO(
                null,
                "Tecnico Inactivo CP17 " + sufijo,
                "INAC800101" + homoclave,
                "9214444444",
                "inactivo" + sufijo + "@empresa.mx",
                TipoEquipo.MECANICO,
                NivelCertificacion.II,
                LocalDate.of(2021, 1, 1),
                EstatusTecnico.INACTIVO
        ));

        System.out.println("Equipo usado:");
        System.out.println(equipo);

        System.out.println("Técnico usado:");
        System.out.println(inactivo);

        try {
            OrdenMantenimientoDTO orden = ordenService.crearOrden(new OrdenMantenimientoDTO(
                    null,
                    equipo.getIdEquipo(),
                    inactivo.getIdTecnico(),
                    "Preventivo",
                    LocalDate.of(2026, 7, 3),
                    null,
                    null,
                    "Orden RN03 inválida",
                    BigDecimal.valueOf(2000),
                    null,
                    EstadoOrden.PROGRAMADA
            ));

            System.out.println("FAIL: se creó una orden con técnico inactivo, esto NO debía pasar.");
            System.out.println(orden);
        } catch (Exception e) {
            System.out.println("PASS: el sistema rechazó correctamente al técnico inactivo.");
            System.out.println("Excepción recibida: " + e.getClass().getSimpleName());
            System.out.println("Mensaje: " + e.getMessage());
        }

        System.out.println("CP-17 ejecutado. Verificar en MySQL que no exista una orden con descripcion_trabajo='Orden RN03 inválida'.");
    }

    private void cp18EliminarEquipoConOrdenes() throws Exception {
        System.out.println("CP-18 — Eliminar equipo con órdenes registradas");

        List<OrdenMantenimientoDTO> ordenes = ordenService.consultarHistorial(null);

        if (ordenes.isEmpty()) {
            System.out.println("FAIL: no hay órdenes registradas para ejecutar CP-18.");
            return;
        }

        OrdenMantenimientoDTO orden = ordenes.get(0);
        Long idEquipoConOrdenes = orden.getIdEquipo();

        System.out.println("Equipo seleccionado para eliminación:");
        System.out.println("idEquipo = " + idEquipoConOrdenes);
        System.out.println("Orden asociada:");
        System.out.println(orden);

        try {
            equipoService.eliminarEquipo(idEquipoConOrdenes);

            System.out.println("FAIL: se eliminó un equipo con órdenes registradas, esto NO debía pasar.");
        } catch (Exception e) {
            System.out.println("PASS: el sistema rechazó correctamente la eliminación del equipo con órdenes.");
            System.out.println("Excepción recibida: " + e.getClass().getSimpleName());
            System.out.println("Mensaje: " + e.getMessage());
        }

        System.out.println("CP-18 ejecutado. Verificar en MySQL que el equipo sigue existiendo.");
    }

    private void cp19FinalizarSinCierre() throws Exception {
        System.out.println("CP-19 - Finalizar orden sin fecha cierre ni costo real");

        String sufijo = String.valueOf(System.currentTimeMillis()).substring(7);
        String homoclave = sufijo.substring(sufijo.length() - 3);

        EquipoDTO equipo = equipoService.registrarEquipo(new EquipoDTO(
                null,
                "Equipo CP19 " + sufijo,
                TipoEquipo.MECANICO,
                "Atlas",
                "GA",
                "SN-CP19-" + sufijo,
                "Planta Norte",
                LocalDate.of(2024, 1, 1),
                "Operativo",
                Criticidad.MEDIA
        ));

        TecnicoDTO tecnico = tecnicoService.registrarTecnico(new TecnicoDTO(
                null,
                "Tecnico CP19 " + sufijo,
                "FINA800101" + homoclave,
                "9215555555",
                "fin" + sufijo + "@empresa.mx",
                TipoEquipo.MECANICO,
                NivelCertificacion.II,
                LocalDate.of(2021, 1, 1),
                EstatusTecnico.ACTIVO
        ));

        OrdenMantenimientoDTO orden = ordenService.crearOrden(new OrdenMantenimientoDTO(
                null,
                equipo.getIdEquipo(),
                tecnico.getIdTecnico(),
                "Preventivo",
                LocalDate.of(2026, 7, 4),
                null,
                null,
                "Orden CP19",
                BigDecimal.valueOf(2000),
                null,
                EstadoOrden.PROGRAMADA
        ));

        OrdenMantenimientoDTO enEjecucion = ordenService.actualizarEstadoOrden(
                orden.getIdOrden(),
                EstadoOrden.EN_EJECUCION,
                LocalDate.of(2026, 7, 4),
                null,
                null
        );

        System.out.println("Orden en ejecucion:");
        System.out.println(enEjecucion);

        try {
            OrdenMantenimientoDTO finalizada = ordenService.actualizarEstadoOrden(
                    orden.getIdOrden(),
                    EstadoOrden.FINALIZADA,
                    LocalDate.of(2026, 7, 4),
                    null,
                    null
            );

            System.out.println("FAIL: se finalizo sin fecha cierre ni costo real, esto NO debia pasar.");
            System.out.println(finalizada);
        } catch (Exception e) {
            System.out.println("PASS: el sistema rechazo correctamente finalizar sin fecha cierre ni costo real.");
            System.out.println("Excepcion recibida: " + e.getClass().getSimpleName());
            System.out.println("Mensaje: " + e.getMessage());
        }

        System.out.println("CP-19 ejecutado. Verificar en MySQL que la orden no quedo FINALIZADA sin datos de cierre.");
    }

    private void cp20CertificacionInsuficiente() throws Exception {
        System.out.println("CP-20 — Técnico nivel I para equipo de criticidad ALTA");

        String sufijo = String.valueOf(System.currentTimeMillis()).substring(7);

        EquipoDTO equipo = equipoService.registrarEquipo(new EquipoDTO(
                null,
                "Equipo Alta CP20 " + sufijo,
                TipoEquipo.HIDRAULICO,
                "Grundfos",
                "CR",
                "SN-CP20-" + sufijo,
                "Planta Norte",
                LocalDate.of(2024, 1, 1),
                "Operativo",
                Criticidad.ALTA
        ));

        TecnicoDTO tecnicoNivelI = tecnicoService.registrarTecnico(new TecnicoDTO(
                null,
                "Tecnico Nivel I CP20 " + sufijo,
                "NIV" + sufijo,
                "9216666666",
                "niveli" + sufijo + "@empresa.mx",
                TipoEquipo.HIDRAULICO,
                NivelCertificacion.I,
                LocalDate.of(2021, 1, 1),
                EstatusTecnico.ACTIVO
        ));

        ordenService.crearOrden(new OrdenMantenimientoDTO(
                null,
                equipo.getIdEquipo(),
                tecnicoNivelI.getIdTecnico(),
                "Preventivo",
                LocalDate.of(2026, 7, 5),
                null,
                null,
                "Orden RN07 inválida",
                BigDecimal.valueOf(2000),
                null,
                EstadoOrden.PROGRAMADA
        ));

        System.out.println("ERROR: se creó orden con técnico nivel I para equipo ALTA, esto NO debía pasar.");
    }

    private void cp21TransicionInvalida() throws Exception {
        System.out.println("CP-21 — Transición inválida de FINALIZADA a EN_EJECUCION");

        String sufijo = String.valueOf(System.currentTimeMillis()).substring(7);

        EquipoDTO equipo = equipoService.registrarEquipo(new EquipoDTO(
                null,
                "Equipo CP21 " + sufijo,
                TipoEquipo.MECANICO,
                "Atlas",
                "GA",
                "SN-CP21-" + sufijo,
                "Planta Norte",
                LocalDate.of(2024, 1, 1),
                "Operativo",
                Criticidad.MEDIA
        ));

        TecnicoDTO tecnico = tecnicoService.registrarTecnico(new TecnicoDTO(
                null,
                "Tecnico CP21 " + sufijo,
                "TRN" + sufijo,
                "9217777777",
                "trn" + sufijo + "@empresa.mx",
                TipoEquipo.MECANICO,
                NivelCertificacion.II,
                LocalDate.of(2021, 1, 1),
                EstatusTecnico.ACTIVO
        ));

        OrdenMantenimientoDTO orden = ordenService.crearOrden(new OrdenMantenimientoDTO(
                null,
                equipo.getIdEquipo(),
                tecnico.getIdTecnico(),
                "Preventivo",
                LocalDate.of(2026, 7, 6),
                null,
                null,
                "Orden CP21",
                BigDecimal.valueOf(2000),
                null,
                EstadoOrden.PROGRAMADA
        ));

        ordenService.actualizarEstadoOrden(
                orden.getIdOrden(),
                EstadoOrden.EN_EJECUCION,
                LocalDate.of(2026, 7, 6),
                null,
                null
        );

        OrdenMantenimientoDTO finalizada = ordenService.actualizarEstadoOrden(
                orden.getIdOrden(),
                EstadoOrden.FINALIZADA,
                LocalDate.of(2026, 7, 6),
                LocalDate.of(2026, 7, 7),
                BigDecimal.valueOf(2100)
        );

        System.out.println("Orden finalizada:");
        System.out.println(finalizada);

        ordenService.actualizarEstadoOrden(
                orden.getIdOrden(),
                EstadoOrden.EN_EJECUCION,
                LocalDate.of(2026, 7, 6),
                null,
                null
        );

        System.out.println("ERROR: se permitió regresar de FINALIZADA a EN_EJECUCION, esto NO debía pasar.");
    }
}