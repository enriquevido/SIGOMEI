package mx.uv.sigomei.remote;

import mx.uv.sigomei.dto.TecnicoDTO;
import mx.uv.sigomei.enums.EstatusTecnico;
import mx.uv.sigomei.enums.NivelCertificacion;
import mx.uv.sigomei.enums.TipoEquipo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ITecnicoService extends Remote {
    TecnicoDTO registrarTecnico(TecnicoDTO tecnico) throws RemoteException;

    TecnicoDTO obtenerTecnicoPorId(Long idTecnico) throws RemoteException;

    List<TecnicoDTO> buscarTecnicos(String nombre, TipoEquipo especialidad,
                                    NivelCertificacion nivel, EstatusTecnico estatus)
            throws RemoteException;

    TecnicoDTO modificarTecnico(TecnicoDTO tecnico) throws RemoteException;

    void eliminarTecnico(Long idTecnico) throws RemoteException;
}
