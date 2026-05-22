package mx.uv.sigomei.remote.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import mx.uv.sigomei.dto.EquipoDTO;
import mx.uv.sigomei.exception.EntidadNoEncontradaException;
import mx.uv.sigomei.mapper.SigomeiMapper;
import mx.uv.sigomei.remote.IEquipoService;
import mx.uv.sigomei.service.EquipoService;

public class EquipoRemoteService extends UnicastRemoteObject implements IEquipoService {
    private static final long serialVersionUID = 1L;
    private final EquipoService equipoService;

    public EquipoRemoteService(EquipoService equipoService) throws RemoteException {
        super();
        this.equipoService = equipoService;
    }

    @Override
    public EquipoDTO registrarEquipo(EquipoDTO equipo) throws RemoteException {
        return SigomeiMapper.toEquipoDTO(equipoService.registrar(SigomeiMapper.toEquipo(equipo)));
    }

    @Override
    public EquipoDTO obtenerEquipoPorId(Long idEquipo) throws RemoteException {
        return equipoService.obtenerPorId(idEquipo)
                .map(SigomeiMapper::toEquipoDTO)
                .orElseThrow(() -> new EntidadNoEncontradaException("Equipo no encontrado: " + idEquipo));
    }

    @Override
    public List<EquipoDTO> buscarEquipos() throws RemoteException {
        return equipoService.buscarTodos().stream().map(SigomeiMapper::toEquipoDTO).toList();
    }

    @Override
    public EquipoDTO modificarEquipo(EquipoDTO equipo) throws RemoteException {
        return SigomeiMapper.toEquipoDTO(equipoService.modificar(SigomeiMapper.toEquipo(equipo)));
    }

    @Override
    public void eliminarEquipo(Long idEquipo) throws RemoteException {
        equipoService.eliminar(idEquipo);
    }
}
