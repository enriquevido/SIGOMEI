# Mapeo de Pruebas a Reglas de Negocio

| RN | Regla de negocio | Clase de prueba | Metodo de prueba | Tecnica de diseno | Resultado esperado en fase ROJO |
|---|---|---|---|---|---|
| RN-01 | Rechazar tecnico con especialidad distinta al tipo de equipo | ValidadorReglasTest | rn01_debeRechazarTecnicoConEspecialidadDistintaAlTipoEquipo | Particion de equivalencias | Falla porque se lanza UnsupportedOperationException en lugar de ReglaNegocioException |
| RN-02 | Rechazar orden activa duplicada para el mismo equipo y fecha | ValidadorReglasTest | rn02_debeRechazarOrdenActivaDuplicadaMismaFecha | Tabla de decision | Falla porque se lanza UnsupportedOperationException en lugar de ReglaNegocioException |
| RN-03 | Rechazar tecnico inactivo | ValidadorReglasTest | rn03_debeRechazarTecnicoInactivo | Particion de equivalencias | Falla porque se lanza UnsupportedOperationException en lugar de ReglaNegocioException |
| RN-04 | Rechazar eliminacion con ordenes registradas | ValidadorReglasTest | rn04_debeRechazarEliminacionConOrdenesRegistradas | Particion de equivalencias | Falla porque se lanza UnsupportedOperationException en lugar de ReglaNegocioException |
| RN-05 | Rechazar fechas incoherentes | ValidadorReglasTest | rn05_debeRechazarFechasIncoherentes | Valores limite / orden temporal | Falla porque se lanza UnsupportedOperationException en lugar de ReglaNegocioException |
| RN-06 | Rechazar orden finalizada sin costo real o fecha de cierre | ValidadorReglasTest | rn06_debeRechazarOrdenFinalizadaSinCostoRealOFechaCierre | Tabla de decision | Falla porque se lanza UnsupportedOperationException en lugar de ReglaNegocioException |
| RN-07 | Rechazar criticidad alta con tecnico certificacion I | ValidadorReglasTest | rn07_debeRechazarCriticidadAltaConTecnicoCertificacionI | Particion de equivalencias | Falla porque se lanza UnsupportedOperationException en lugar de ReglaNegocioException |
| RN-08 | Rechazar transicion de FINALIZADA a EN_EJECUCION | GestorEstadoOrdenTest | rn08_debeRechazarTransicionFinalizadaAEnEjecucion | Transicion de estados | Falla porque se lanza UnsupportedOperationException en lugar de TransicionEstadoException |
