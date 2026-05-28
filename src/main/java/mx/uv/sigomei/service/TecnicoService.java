package mx.uv.sigomei.service;

import mx.uv.sigomei.domain.Tecnico;
import mx.uv.sigomei.enums.EstatusTecnico;
import mx.uv.sigomei.enums.NivelCertificacion;
import mx.uv.sigomei.enums.TipoEquipo;

import java.util.List;
import java.util.Optional;

public interface TecnicoService {
    Tecnico registrar(Tecnico tecnico);

    Optional<Tecnico> obtenerPorId(Long id);

    List<Tecnico> buscarTodos();

    List<Tecnico> buscarConFiltros(String nombre, TipoEquipo especialidad,
                                   NivelCertificacion nivel, EstatusTecnico estatus);

    Tecnico modificar(Tecnico tecnico);

    void eliminar(Long id);
}
