package mx.uv.sigomei.remote.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import mx.uv.sigomei.dto.TecnicoDTO;
import mx.uv.sigomei.exception.EntidadNoEncontradaException;
import mx.uv.sigomei.mapper.SigomeiMapper;
import mx.uv.sigomei.remote.ITecnicoService;
import mx.uv.sigomei.service.TecnicoService;

public class TecnicoRemoteService extends UnicastRemoteObject implements ITecnicoService {
    private static final long serialVersionUID = 1L;
    private final TecnicoService tecnicoService;

    public TecnicoRemoteService(TecnicoService tecnicoService) throws RemoteException {
        super();
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
    public List<TecnicoDTO> buscarTecnicos() throws RemoteException {
        return tecnicoService.buscarTodos().stream().map(SigomeiMapper::toTecnicoDTO).toList();
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
