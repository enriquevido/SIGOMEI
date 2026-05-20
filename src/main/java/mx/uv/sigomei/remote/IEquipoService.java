package mx.uv.sigomei.remote;

import mx.uv.sigomei.dto.EquipoDTO;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IEquipoService extends Remote {
    EquipoDTO registrarEquipo(EquipoDTO equipo) throws RemoteException;

    EquipoDTO obtenerEquipoPorId(Long idEquipo) throws RemoteException;

    List<EquipoDTO> buscarEquipos() throws RemoteException;

    EquipoDTO modificarEquipo(EquipoDTO equipo) throws RemoteException;

    void eliminarEquipo(Long idEquipo) throws RemoteException;
}
