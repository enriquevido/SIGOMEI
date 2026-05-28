package mx.uv.sigomei.remote.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

import mx.uv.sigomei.dto.TecnicoDTO;
import mx.uv.sigomei.enums.EstatusTecnico;
import mx.uv.sigomei.enums.NivelCertificacion;
import mx.uv.sigomei.enums.TipoEquipo;
import mx.uv.sigomei.exception.EntidadNoEncontradaException;
import mx.uv.sigomei.mapper.SigomeiMapper;
import mx.uv.sigomei.remote.ITecnicoService;
import mx.uv.sigomei.service.TecnicoService;

public class TecnicoRemoteService extends UnicastRemoteObject implements ITecnicoService {
    private static final long serialVersionUID = 1L;
    private final TecnicoService tecnicoService;

    public TecnicoRemoteService(TecnicoService tecnicoService) throws RemoteException {
        this(tecnicoService, false);
    }

    public TecnicoRemoteService(TecnicoService tecnicoService, boolean sslEnabled) throws RemoteException {
        this(tecnicoService, 0, sslEnabled);
    }

    public TecnicoRemoteService(TecnicoService tecnicoService, int port, boolean sslEnabled) throws RemoteException {
        super(port,
                sslEnabled ? new SslRMIClientSocketFactory() : null,
                sslEnabled ? new SslRMIServerSocketFactory() : null);
        this.tecnicoService = tecnicoService;
    }

    @Override
    public TecnicoDTO registrarTecnico(TecnicoDTO tecnico) throws RemoteException {
        return SigomeiMapper.toTecnicoDTO(tecnicoService.registrar(SigomeiMapper.toTecnico(tecnico)));
    }

    @Override
    public TecnicoDTO obtenerTecnicoPorId(Long idTecnico) throws RemoteException {
        return tecnicoService.obtenerPorId(idTecnico)
                .map(SigomeiMapper::toTecnicoDTO)
                .orElseThrow(() -> new EntidadNoEncontradaException("Tecnico no encontrado: " + idTecnico));
    }

    @Override
    public List<TecnicoDTO> buscarTecnicos(String nombre, TipoEquipo especialidad,
                                           NivelCertificacion nivel, EstatusTecnico estatus)
            throws RemoteException {
        return tecnicoService.buscarConFiltros(nombre, especialidad, nivel, estatus).stream()
                .map(SigomeiMapper::toTecnicoDTO)
                .toList();
    }

    @Override
    public TecnicoDTO modificarTecnico(TecnicoDTO tecnico) throws RemoteException {
        return SigomeiMapper.toTecnicoDTO(tecnicoService.modificar(SigomeiMapper.toTecnico(tecnico)));
    }

    @Override
    public void eliminarTecnico(Long idTecnico) throws RemoteException {
        tecnicoService.eliminar(idTecnico);
    }
}
