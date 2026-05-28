package mx.uv.sigomei.remote;

import mx.uv.sigomei.dto.EquipoDTO;
import mx.uv.sigomei.enums.Criticidad;
import mx.uv.sigomei.enums.TipoEquipo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IEquipoService extends Remote {
    EquipoDTO registrarEquipo(EquipoDTO equipo) throws RemoteException;

    EquipoDTO obtenerEquipoPorId(Long idEquipo) throws RemoteException;

    List<EquipoDTO> buscarEquipos(String nombre, TipoEquipo tipo, String numeroSerie,
                                  String ubicacion, String estadoOperativo, Criticidad criticidad)
            throws RemoteException;

    EquipoDTO modificarEquipo(EquipoDTO equipo) throws RemoteException;

    void eliminarEquipo(Long idEquipo) throws RemoteException;
}
