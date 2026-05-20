package mx.uv.sigomei.remote;

import mx.uv.sigomei.dto.FiltroOrdenDTO;
import mx.uv.sigomei.dto.OrdenMantenimientoDTO;
import mx.uv.sigomei.enums.EstadoOrden;

import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface IOrdenService extends Remote {
    OrdenMantenimientoDTO crearOrden(OrdenMantenimientoDTO orden) throws RemoteException;

    OrdenMantenimientoDTO asignarTecnico(Long idOrden, Long idTecnico) throws RemoteException;

    OrdenMantenimientoDTO actualizarEstadoOrden(Long idOrden, EstadoOrden nuevoEstado, LocalDate fechaInicio,
                                                LocalDate fechaCierre, BigDecimal costoReal)
            throws RemoteException;

    OrdenMantenimientoDTO cancelarOrden(Long idOrden) throws RemoteException;

    OrdenMantenimientoDTO obtenerOrdenPorId(Long idOrden) throws RemoteException;

    List<OrdenMantenimientoDTO> consultarHistorial(FiltroOrdenDTO filtro) throws RemoteException;
}
