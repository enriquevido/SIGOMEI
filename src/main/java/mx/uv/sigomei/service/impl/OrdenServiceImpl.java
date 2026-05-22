package mx.uv.sigomei.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import mx.uv.sigomei.domain.Equipo;
import mx.uv.sigomei.domain.OrdenMantenimiento;
import mx.uv.sigomei.domain.Tecnico;
import mx.uv.sigomei.enums.EstadoOrden;
import mx.uv.sigomei.exception.EntidadNoEncontradaException;
import mx.uv.sigomei.exception.ReglaNegocioException;
import mx.uv.sigomei.exception.ValidacionException;
import mx.uv.sigomei.repository.OrdenRepository;
import mx.uv.sigomei.service.OrdenService;
import mx.uv.sigomei.state.GestorEstadoOrden;
import mx.uv.sigomei.validator.ValidadorReglas;

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
        validarOrdenBase(orden);

        if (orden.getEstadoOrden() == null) {
            orden.setEstadoOrden(EstadoOrden.PROGRAMADA);
        }

        validadorReglas.validarTecnicoActivo(orden.getTecnico());
        validadorReglas.validarEspecialidad(orden.getEquipo(), orden.getTecnico());
        validadorReglas.validarCriticidadAlta(orden.getEquipo(), orden.getTecnico());
        validadorReglas.validarOrdenActivaDuplicada(
                orden.getEquipo(),
                orden.getFechaProgramada(),
                ordenRepository.findByEquipo(orden.getEquipo())
        );
        validadorReglas.validarFechas(orden);
        validadorReglas.validarDatosFinalizacion(orden);

        return ordenRepository.save(orden);
    }

    @Override
    public OrdenMantenimiento asignarTecnico(Long idOrden, Tecnico tecnico) {
        OrdenMantenimiento orden = obtenerOrdenObligatoria(idOrden);

        if (orden.getEstadoOrden() != EstadoOrden.PROGRAMADA) {
            throw new ReglaNegocioException("RN-08: solo se puede cambiar tecnico en una orden PROGRAMADA");
        }

        validadorReglas.validarTecnicoActivo(tecnico);
        validadorReglas.validarEspecialidad(orden.getEquipo(), tecnico);
        validadorReglas.validarCriticidadAlta(orden.getEquipo(), tecnico);

        orden.setTecnico(tecnico);

        return ordenRepository.save(orden);
    }

    @Override
    public OrdenMantenimiento cambiarEstado(
            Long idOrden,
            EstadoOrden nuevoEstado,
            LocalDate fechaInicio,
            LocalDate fechaCierre,
            BigDecimal costoReal
    ) {
        OrdenMantenimiento orden = obtenerOrdenObligatoria(idOrden);

        gestorEstadoOrden.validarTransicion(orden.getEstadoOrden(), nuevoEstado);

        if (nuevoEstado == EstadoOrden.EN_EJECUCION) {
            if (fechaInicio == null) {
                throw new ReglaNegocioException("RN-05: la fecha de inicio es obligatoria al iniciar una orden");
            }

            orden.setFechaInicio(fechaInicio);
        }

        if (nuevoEstado == EstadoOrden.FINALIZADA) {
            if (orden.getFechaInicio() == null && fechaInicio != null) {
                orden.setFechaInicio(fechaInicio);
            }

            orden.setFechaCierre(fechaCierre);
            orden.setCostoReal(costoReal);
        }

        orden.setEstadoOrden(nuevoEstado);

        validadorReglas.validarFechas(orden);
        validadorReglas.validarDatosFinalizacion(orden);

        return ordenRepository.save(orden);
    }

    @Override
    public OrdenMantenimiento cancelar(Long idOrden) {
        OrdenMantenimiento orden = obtenerOrdenObligatoria(idOrden);

        gestorEstadoOrden.validarTransicion(orden.getEstadoOrden(), EstadoOrden.CANCELADA);

        orden.setEstadoOrden(EstadoOrden.CANCELADA);

        return ordenRepository.save(orden);
    }

    @Override
    public Optional<OrdenMantenimiento> obtenerPorId(Long id) {
        if (id == null) {
            throw new ValidacionException("El id de la orden es obligatorio");
        }

        return ordenRepository.findById(id);
    }

    @Override
    public List<OrdenMantenimiento> consultarHistorial() {
        return ordenRepository.findAll().stream()
                .sorted(Comparator.comparing(
                        OrdenMantenimiento::getFechaProgramada,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ).reversed())
                .toList();
    }

    private OrdenMantenimiento obtenerOrdenObligatoria(Long idOrden) {
        if (idOrden == null) {
            throw new ValidacionException("El id de la orden es obligatorio");
        }

        return ordenRepository.findById(idOrden)
                .orElseThrow(() -> new EntidadNoEncontradaException("Orden no encontrada con id: " + idOrden));
    }

    private void validarOrdenBase(OrdenMantenimiento orden) {
        if (orden == null) {
            throw new ValidacionException("La orden es obligatoria");
        }

        Equipo equipo = orden.getEquipo();
        Tecnico tecnico = orden.getTecnico();

        if (equipo == null || equipo.getId() == null) {
            throw new ValidacionException("La orden requiere un equipo registrado");
        }

        if (tecnico == null || tecnico.getId() == null) {
            throw new ValidacionException("La orden requiere un tecnico registrado");
        }

        if (esBlanco(orden.getTipoMantenimiento())) {
            throw new ValidacionException("El tipo de mantenimiento es obligatorio");
        }

        if (orden.getFechaProgramada() == null) {
            throw new ValidacionException("La fecha programada es obligatoria");
        }

        if (esBlanco(orden.getDescripcionTrabajo())) {
            throw new ValidacionException("La descripcion del trabajo es obligatoria");
        }

        if (orden.getCostoEstimado() == null || orden.getCostoEstimado().signum() < 0) {
            throw new ValidacionException("El costo estimado es obligatorio y no puede ser negativo");
        }
    }

    private boolean esBlanco(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}