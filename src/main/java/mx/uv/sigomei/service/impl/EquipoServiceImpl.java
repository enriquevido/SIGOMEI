package mx.uv.sigomei.service.impl;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import mx.uv.sigomei.domain.Equipo;
import mx.uv.sigomei.domain.OrdenMantenimiento;
import mx.uv.sigomei.enums.Criticidad;
import mx.uv.sigomei.enums.TipoEquipo;
import mx.uv.sigomei.exception.EntidadDuplicadaException;
import mx.uv.sigomei.exception.EntidadNoEncontradaException;
import mx.uv.sigomei.exception.ValidacionException;
import mx.uv.sigomei.repository.EquipoRepository;
import mx.uv.sigomei.repository.OrdenRepository;
import mx.uv.sigomei.service.EquipoService;
import mx.uv.sigomei.util.AuditLogger;
import mx.uv.sigomei.validator.ValidadorReglas;

import java.util.logging.Level;

public class EquipoServiceImpl implements EquipoService {

    private final EquipoRepository equipoRepository;
    private final OrdenRepository ordenRepository;
    private final ValidadorReglas validadorReglas;

    public EquipoServiceImpl(EquipoRepository equipoRepository,
                             OrdenRepository ordenRepository,
                             ValidadorReglas validadorReglas) {
        this.equipoRepository = equipoRepository;
        this.ordenRepository = ordenRepository;
        this.validadorReglas = validadorReglas;
    }

    @Override
    public Equipo registrar(Equipo equipo) {
        validarEquipo(equipo);
        validarNumeroSerieUnico(equipo);

        Equipo resultado = equipoRepository.save(equipo);
        AuditLogger.log(Level.INFO, "EquipoServiceImpl", "Equipo registrado con id: " + resultado.getId());
        return resultado;
    }

    @Override
    public Optional<Equipo> obtenerPorId(Long id) {
        if (id == null) {
            throw new ValidacionException("El id del equipo es obligatorio");
        }

        AuditLogger.log(Level.INFO, "EquipoServiceImpl", "Consulta de equipo por id: " + id);
        return equipoRepository.findById(id);
    }

    @Override
    public List<Equipo> buscarTodos() {
        AuditLogger.log(Level.INFO, "EquipoServiceImpl", "Consulta de todos los equipos");
        return equipoRepository.findAll();
    }

    @Override
    public List<Equipo> buscarConFiltros(String nombre, TipoEquipo tipo, String numeroSerie,
                                         String ubicacion, String estadoOperativo, Criticidad criticidad) {
        AuditLogger.log(Level.INFO, "EquipoServiceImpl", "Consulta de equipos con filtros");
        return equipoRepository.findAll().stream()
                .filter(equipo -> contieneIgnorandoMayusculas(equipo.getNombre(), nombre))
                .filter(equipo -> tipo == null || equipo.getTipo() == tipo)
                .filter(equipo -> contieneIgnorandoMayusculas(equipo.getNumeroSerie(), numeroSerie))
                .filter(equipo -> contieneIgnorandoMayusculas(equipo.getUbicacionPlanta(), ubicacion))
                .filter(equipo -> contieneIgnorandoMayusculas(equipo.getEstadoOperativo(), estadoOperativo))
                .filter(equipo -> criticidad == null || equipo.getCriticidad() == criticidad)
                .toList();
    }

    @Override
    public Equipo modificar(Equipo equipo) {
        validarEquipo(equipo);

        if (equipo.getId() == null) {
            throw new ValidacionException("El id del equipo es obligatorio para modificar");
        }

        equipoRepository.findById(equipo.getId())
                .orElseThrow(() -> new EntidadNoEncontradaException(
                        "Equipo no encontrado con id: " + equipo.getId()
                ));

        validarNumeroSerieUnicoAlModificar(equipo);

        Equipo resultado = equipoRepository.save(equipo);
        AuditLogger.log(Level.INFO, "EquipoServiceImpl", "Equipo modificado con id: " + resultado.getId());
        return resultado;
    }

    @Override
    public void eliminar(Long id) {
        if (id == null) {
            throw new ValidacionException("El id del equipo es obligatorio para eliminar");
        }

        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException(
                        "Equipo no encontrado con id: " + id
                ));

        List<OrdenMantenimiento> ordenesDelEquipo = ordenRepository.findByEquipo(equipo);

        validadorReglas.validarEliminacionSinOrdenes(ordenesDelEquipo);

        equipoRepository.deleteById(id);
        AuditLogger.log(Level.INFO, "EquipoServiceImpl", "Equipo eliminado con id: " + id);
    }

    private void validarEquipo(Equipo equipo) {
        if (equipo == null) {
            throw new ValidacionException("El equipo es obligatorio");
        }

        if (esBlanco(equipo.getNombre())) {
            throw new ValidacionException("El nombre del equipo es obligatorio");
        }

        if (equipo.getTipo() == null) {
            throw new ValidacionException("El tipo del equipo es obligatorio");
        }

        if (esBlanco(equipo.getMarca())) {
            throw new ValidacionException("La marca del equipo es obligatoria");
        }

        if (esBlanco(equipo.getModelo())) {
            throw new ValidacionException("El modelo del equipo es obligatorio");
        }

        if (esBlanco(equipo.getNumeroSerie())) {
            throw new ValidacionException("El numero de serie del equipo es obligatorio");
        }

        if (esBlanco(equipo.getUbicacionPlanta())) {
            throw new ValidacionException("La ubicacion en planta es obligatoria");
        }

        if (equipo.getFechaInstalacion() == null) {
            throw new ValidacionException("La fecha de instalacion es obligatoria");
        }

        if (esBlanco(equipo.getEstadoOperativo())) {
            throw new ValidacionException("El estado operativo es obligatorio");
        }

        if (equipo.getCriticidad() == null) {
            throw new ValidacionException("La criticidad del equipo es obligatoria");
        }
    }

    private void validarNumeroSerieUnico(Equipo equipo) {
        boolean existeNumeroSerie = equipoRepository.findAll().stream()
                .anyMatch(equipoExistente ->
                        equipoExistente.getNumeroSerie() != null
                                && equipoExistente.getNumeroSerie().equalsIgnoreCase(equipo.getNumeroSerie())
                );

        if (existeNumeroSerie) {
            throw new EntidadDuplicadaException("Ya existe un equipo con el numero de serie: " + equipo.getNumeroSerie());
        }
    }

    private void validarNumeroSerieUnicoAlModificar(Equipo equipo) {
        boolean existeNumeroSerieEnOtroEquipo = equipoRepository.findAll().stream()
                .anyMatch(equipoExistente ->
                        equipoExistente.getNumeroSerie() != null
                                && equipoExistente.getNumeroSerie().equalsIgnoreCase(equipo.getNumeroSerie())
                                && !equipoExistente.getId().equals(equipo.getId())
                );

        if (existeNumeroSerieEnOtroEquipo) {
            throw new EntidadDuplicadaException("Ya existe otro equipo con el numero de serie: " + equipo.getNumeroSerie());
        }
    }

    private boolean esBlanco(String valor) {
        return valor == null || valor.trim().isEmpty();
    }

    private boolean contieneIgnorandoMayusculas(String valor, String filtro) {
        if (esBlanco(filtro)) {
            return true;
        }
        if (valor == null) {
            return false;
        }
        return valor.toLowerCase(Locale.ROOT).contains(filtro.trim().toLowerCase(Locale.ROOT));
    }
}
