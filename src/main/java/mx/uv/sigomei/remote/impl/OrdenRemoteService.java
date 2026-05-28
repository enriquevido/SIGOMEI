package mx.uv.sigomei.remote.impl;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.List;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

import mx.uv.sigomei.domain.Equipo;
import mx.uv.sigomei.domain.Tecnico;
import mx.uv.sigomei.dto.FiltroOrdenDTO;
import mx.uv.sigomei.dto.OrdenMantenimientoDTO;
import mx.uv.sigomei.enums.EstadoOrden;
import mx.uv.sigomei.exception.EntidadNoEncontradaException;
import mx.uv.sigomei.mapper.SigomeiMapper;
import mx.uv.sigomei.remote.IOrdenService;
import mx.uv.sigomei.service.EquipoService;
import mx.uv.sigomei.service.OrdenService;
import mx.uv.sigomei.service.TecnicoService;

public class OrdenRemoteService extends UnicastRemoteObject implements IOrdenService {
    private static final long serialVersionUID = 1L;
    private final OrdenService ordenService;
    private final EquipoService equipoService;
    private final TecnicoService tecnicoService;

    public OrdenRemoteService(OrdenService ordenService, EquipoService equipoService, TecnicoService tecnicoService)
            throws RemoteException {
        this(ordenService, equipoService, tecnicoService, false);
    }

    public OrdenRemoteService(OrdenService ordenService, EquipoService equipoService, TecnicoService tecnicoService,
                              boolean sslEnabled)
            throws RemoteException {
        this(ordenService, equipoService, tecnicoService, 0, sslEnabled);
    }

    public OrdenRemoteService(OrdenService ordenService, EquipoService equipoService, TecnicoService tecnicoService,
                              int port, boolean sslEnabled)
            throws RemoteException {
        super(port,
                sslEnabled ? new SslRMIClientSocketFactory() : null,
                sslEnabled ? new SslRMIServerSocketFactory() : null);
        this.ordenService = ordenService;
        this.equipoService = equipoService;
        this.tecnicoService = tecnicoService;
    }

    @Override
    public OrdenMantenimientoDTO crearOrden(OrdenMantenimientoDTO orden) throws RemoteException {
        Equipo equipo = obtenerEquipo(orden.getIdEquipo());
        Tecnico tecnico = obtenerTecnico(orden.getIdTecnico());
        return SigomeiMapper.toOrdenDTO(ordenService.crear(SigomeiMapper.toOrden(orden, equipo, tecnico)));
    }

    @Override
    public OrdenMantenimientoDTO asignarTecnico(Long idOrden, Long idTecnico) throws RemoteException {
        return SigomeiMapper.toOrdenDTO(ordenService.asignarTecnico(idOrden, obtenerTecnico(idTecnico)));
    }

    @Override
    public OrdenMantenimientoDTO actualizarEstadoOrden(Long idOrden, EstadoOrden nuevoEstado, LocalDate fechaInicio,
                                                       LocalDate fechaCierre, BigDecimal costoReal)
            throws RemoteException {
        return SigomeiMapper.toOrdenDTO(
                ordenService.cambiarEstado(idOrden, nuevoEstado, fechaInicio, fechaCierre, costoReal));
    }

    @Override
    public OrdenMantenimientoDTO cancelarOrden(Long idOrden) throws RemoteException {
        return SigomeiMapper.toOrdenDTO(ordenService.cancelar(idOrden));
    }

    @Override
    public OrdenMantenimientoDTO obtenerOrdenPorId(Long idOrden) throws RemoteException {
        return ordenService.obtenerPorId(idOrden)
                .map(SigomeiMapper::toOrdenDTO)
                .orElseThrow(() -> new EntidadNoEncontradaException("Orden no encontrada: " + idOrden));
    }

    @Override
    public List<OrdenMantenimientoDTO> consultarHistorial(FiltroOrdenDTO filtro) throws RemoteException {
        return ordenService.consultarHistorial(filtro).stream()
                .map(SigomeiMapper::toOrdenDTO)
                .toList();
    }

    private Equipo obtenerEquipo(Long idEquipo) {
        return equipoService.obtenerPorId(idEquipo)
                .orElseThrow(() -> new EntidadNoEncontradaException("Equipo no encontrado: " + idEquipo));
    }

    private Tecnico obtenerTecnico(Long idTecnico) {
        return tecnicoService.obtenerPorId(idTecnico)
                .orElseThrow(() -> new EntidadNoEncontradaException("Tecnico no encontrado: " + idTecnico));
    }
}
