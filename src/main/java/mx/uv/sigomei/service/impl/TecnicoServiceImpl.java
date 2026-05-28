package mx.uv.sigomei.service.impl;

import java.util.List;
import java.util.Optional;

import mx.uv.sigomei.domain.OrdenMantenimiento;
import mx.uv.sigomei.domain.Tecnico;
import mx.uv.sigomei.exception.EntidadDuplicadaException;
import mx.uv.sigomei.exception.EntidadNoEncontradaException;
import mx.uv.sigomei.exception.ValidacionException;
import mx.uv.sigomei.repository.OrdenRepository;
import mx.uv.sigomei.repository.TecnicoRepository;
import mx.uv.sigomei.service.TecnicoService;
import mx.uv.sigomei.util.AuditLogger;
import mx.uv.sigomei.validator.ValidadorReglas;

import java.util.logging.Level;

public class TecnicoServiceImpl implements TecnicoService {

    private final TecnicoRepository tecnicoRepository;
    private final OrdenRepository ordenRepository;
    private final ValidadorReglas validadorReglas;

    public TecnicoServiceImpl(TecnicoRepository tecnicoRepository,
                              OrdenRepository ordenRepository,
                              ValidadorReglas validadorReglas) {
        this.tecnicoRepository = tecnicoRepository;
        this.ordenRepository = ordenRepository;
        this.validadorReglas = validadorReglas;
    }

    @Override
    public Tecnico registrar(Tecnico tecnico) {
        validarTecnico(tecnico);
        validarRfcUnico(tecnico);

        Tecnico resultado = tecnicoRepository.save(tecnico);
        AuditLogger.log(Level.INFO, "TecnicoServiceImpl", "Tecnico registrado con id: " + resultado.getId());
        return resultado;
    }

    @Override
    public Optional<Tecnico> obtenerPorId(Long id) {
        if (id == null) {
            throw new ValidacionException("El id del tecnico es obligatorio");
        }

        AuditLogger.log(Level.INFO, "TecnicoServiceImpl", "Consulta de tecnico por id: " + id);
        return tecnicoRepository.findById(id);
    }

    @Override
    public List<Tecnico> buscarTodos() {
        AuditLogger.log(Level.INFO, "TecnicoServiceImpl", "Consulta de todos los tecnicos");
        return tecnicoRepository.findAll();
    }

    @Override
    public Tecnico modificar(Tecnico tecnico) {
        validarTecnico(tecnico);

        if (tecnico.getId() == null) {
            throw new ValidacionException("El id del tecnico es obligatorio para modificar");
        }

        tecnicoRepository.findById(tecnico.getId())
                .orElseThrow(() -> new EntidadNoEncontradaException(
                        "Tecnico no encontrado con id: " + tecnico.getId()
                ));

        validarRfcUnicoAlModificar(tecnico);

        Tecnico resultado = tecnicoRepository.save(tecnico);
        AuditLogger.log(Level.INFO, "TecnicoServiceImpl", "Tecnico modificado con id: " + resultado.getId());
        return resultado;
    }

    @Override
    public void eliminar(Long id) {
        if (id == null) {
            throw new ValidacionException("El id del tecnico es obligatorio para eliminar");
        }

        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException(
                        "Tecnico no encontrado con id: " + id
                ));

        List<OrdenMantenimiento> ordenesDelTecnico = ordenRepository.findByTecnico(tecnico);

        validadorReglas.validarEliminacionSinOrdenes(ordenesDelTecnico);

        tecnicoRepository.deleteById(id);
        AuditLogger.log(Level.INFO, "TecnicoServiceImpl", "Tecnico eliminado con id: " + id);
    }

    private void validarTecnico(Tecnico tecnico) {
        if (tecnico == null) {
            throw new ValidacionException("El tecnico es obligatorio");
        }

        if (esBlanco(tecnico.getNombreCompleto())) {
            throw new ValidacionException("El nombre completo del tecnico es obligatorio");
        }

        if (esBlanco(tecnico.getRfc())) {
            throw new ValidacionException("El RFC del tecnico es obligatorio");
        }

        if (!tieneFormatoRfcBasico(tecnico.getRfc())) {
            throw new ValidacionException("El RFC del tecnico no tiene formato valido");
        }

        if (esBlanco(tecnico.getTelefono())) {
            throw new ValidacionException("El telefono del tecnico es obligatorio");
        }

        if (!tecnico.getTelefono().matches("\\d{10}")) {
            throw new ValidacionException("El telefono del tecnico debe tener 10 digitos");
        }

        if (esBlanco(tecnico.getCorreo())) {
            throw new ValidacionException("El correo del tecnico es obligatorio");
        }

        if (!tecnico.getCorreo().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new ValidacionException("El correo del tecnico no tiene formato valido");
        }

        if (tecnico.getEspecialidad() == null) {
            throw new ValidacionException("La especialidad del tecnico es obligatoria");
        }

        if (tecnico.getNivelCertificacion() == null) {
            throw new ValidacionException("El nivel de certificacion del tecnico es obligatorio");
        }

        if (tecnico.getFechaIngreso() == null) {
            throw new ValidacionException("La fecha de ingreso del tecnico es obligatoria");
        }

        if (tecnico.getEstatus() == null) {
            throw new ValidacionException("El estatus del tecnico es obligatorio");
        }
    }

    private void validarRfcUnico(Tecnico tecnico) {
        boolean existeRfc = tecnicoRepository.findAll().stream()
                .anyMatch(tecnicoExistente ->
                        tecnicoExistente.getRfc() != null
                                && tecnicoExistente.getRfc().equalsIgnoreCase(tecnico.getRfc())
                );

        if (existeRfc) {
            throw new EntidadDuplicadaException("Ya existe un tecnico con el RFC: " + tecnico.getRfc());
        }
    }

    private void validarRfcUnicoAlModificar(Tecnico tecnico) {
        boolean existeRfcEnOtroTecnico = tecnicoRepository.findAll().stream()
                .anyMatch(tecnicoExistente ->
                        tecnicoExistente.getRfc() != null
                                && tecnicoExistente.getRfc().equalsIgnoreCase(tecnico.getRfc())
                                && !tecnicoExistente.getId().equals(tecnico.getId())
                );

        if (existeRfcEnOtroTecnico) {
            throw new EntidadDuplicadaException("Ya existe otro tecnico con el RFC: " + tecnico.getRfc());
        }
    }

    private boolean tieneFormatoRfcBasico(String rfc) {
        return rfc != null && rfc.matches("^[A-ZÑ&]{4}\\d{6}.*$");
    }

    private boolean esBlanco(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}