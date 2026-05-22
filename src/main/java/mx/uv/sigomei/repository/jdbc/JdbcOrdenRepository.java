package mx.uv.sigomei.repository.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import mx.uv.sigomei.domain.Equipo;
import mx.uv.sigomei.domain.OrdenMantenimiento;
import mx.uv.sigomei.domain.Tecnico;
import mx.uv.sigomei.enums.Criticidad;
import mx.uv.sigomei.enums.EstadoOrden;
import mx.uv.sigomei.enums.EstatusTecnico;
import mx.uv.sigomei.enums.NivelCertificacion;
import mx.uv.sigomei.enums.TipoEquipo;
import mx.uv.sigomei.repository.OrdenRepository;

public class JdbcOrdenRepository implements OrdenRepository {

    private final DatabaseConnectionProvider connectionProvider;

    public JdbcOrdenRepository(DatabaseConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public Optional<OrdenMantenimiento> findById(Long id) {
        String sql = baseSelect() + " WHERE o.id_orden = ?";

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
                return Optional.empty();
            }

        } catch (Exception e) {
            throw new IllegalStateException("Error al buscar orden por id: " + id, e);
        }
    }

    @Override
    public OrdenMantenimiento save(OrdenMantenimiento orden) {
        if (orden.getId() == null) {
            return insert(orden);
        }
        return update(orden);
    }

    @Override
    public List<OrdenMantenimiento> findByEquipo(Equipo equipo) {
        if (equipo == null || equipo.getId() == null) {
            return List.of();
        }

        String sql = baseSelect() + " WHERE o.id_equipo = ? ORDER BY o.fecha_programada DESC";

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, equipo.getId());
            return executeList(statement);

        } catch (Exception e) {
            throw new IllegalStateException("Error al buscar ordenes por equipo", e);
        }
    }

    @Override
    public List<OrdenMantenimiento> findByTecnico(Tecnico tecnico) {
        if (tecnico == null || tecnico.getId() == null) {
            return List.of();
        }

        String sql = baseSelect() + " WHERE o.id_tecnico = ? ORDER BY o.fecha_programada DESC";

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, tecnico.getId());
            return executeList(statement);

        } catch (Exception e) {
            throw new IllegalStateException("Error al buscar ordenes por tecnico", e);
        }
    }

    @Override
    public List<OrdenMantenimiento> findAll() {
        String sql = baseSelect() + " ORDER BY o.fecha_programada DESC, o.id_orden DESC";

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            return executeList(statement);

        } catch (Exception e) {
            throw new IllegalStateException("Error al listar ordenes", e);
        }
    }

    private OrdenMantenimiento insert(OrdenMantenimiento orden) {
        String sql = """
                INSERT INTO orden_mantenimiento (
                    id_equipo, id_tecnico, tipo_mantenimiento, fecha_programada,
                    fecha_inicio, fecha_cierre, descripcion_trabajo,
                    costo_estimado, costo_real, estado_orden
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            fillStatement(statement, orden);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    orden.setId(keys.getLong(1));
                }
            }

            return orden;

        } catch (Exception e) {
            throw new IllegalStateException("Error al insertar orden", e);
        }
    }

    private OrdenMantenimiento update(OrdenMantenimiento orden) {
        String sql = """
                UPDATE orden_mantenimiento
                SET id_equipo = ?,
                    id_tecnico = ?,
                    tipo_mantenimiento = ?,
                    fecha_programada = ?,
                    fecha_inicio = ?,
                    fecha_cierre = ?,
                    descripcion_trabajo = ?,
                    costo_estimado = ?,
                    costo_real = ?,
                    estado_orden = ?
                WHERE id_orden = ?
                """;

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            fillStatement(statement, orden);
            statement.setLong(11, orden.getId());
            statement.executeUpdate();

            return orden;

        } catch (Exception e) {
            throw new IllegalStateException("Error al actualizar orden con id: " + orden.getId(), e);
        }
    }

    private void fillStatement(PreparedStatement statement, OrdenMantenimiento orden) throws Exception {
        statement.setLong(1, orden.getEquipo().getId());
        statement.setLong(2, orden.getTecnico().getId());
        statement.setString(3, orden.getTipoMantenimiento());
        statement.setDate(4, Date.valueOf(orden.getFechaProgramada()));
        statement.setDate(5, orden.getFechaInicio() == null ? null : Date.valueOf(orden.getFechaInicio()));
        statement.setDate(6, orden.getFechaCierre() == null ? null : Date.valueOf(orden.getFechaCierre()));
        statement.setString(7, orden.getDescripcionTrabajo());
        statement.setBigDecimal(8, orden.getCostoEstimado());
        statement.setBigDecimal(9, orden.getCostoReal());
        statement.setString(10, orden.getEstadoOrden().name());
    }

    private List<OrdenMantenimiento> executeList(PreparedStatement statement) throws Exception {
        List<OrdenMantenimiento> ordenes = new ArrayList<>();

        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                ordenes.add(mapRow(resultSet));
            }
        }

        return ordenes;
    }

    private String baseSelect() {
        return """
                SELECT
                    o.id_orden,
                    o.tipo_mantenimiento,
                    o.fecha_programada,
                    o.fecha_inicio,
                    o.fecha_cierre,
                    o.descripcion_trabajo,
                    o.costo_estimado,
                    o.costo_real,
                    o.estado_orden,

                    e.id_equipo,
                    e.nombre AS equipo_nombre,
                    e.tipo AS equipo_tipo,
                    e.marca AS equipo_marca,
                    e.modelo AS equipo_modelo,
                    e.numero_serie AS equipo_numero_serie,
                    e.ubicacion_planta AS equipo_ubicacion_planta,
                    e.fecha_instalacion AS equipo_fecha_instalacion,
                    e.estado_operativo AS equipo_estado_operativo,
                    e.criticidad AS equipo_criticidad,

                    t.id_tecnico,
                    t.nombre_completo AS tecnico_nombre_completo,
                    t.rfc AS tecnico_rfc,
                    t.telefono AS tecnico_telefono,
                    t.correo AS tecnico_correo,
                    t.especialidad AS tecnico_especialidad,
                    t.nivel_certificacion AS tecnico_nivel_certificacion,
                    t.fecha_ingreso AS tecnico_fecha_ingreso,
                    t.estatus AS tecnico_estatus
                FROM orden_mantenimiento o
                INNER JOIN equipo e ON o.id_equipo = e.id_equipo
                INNER JOIN tecnico t ON o.id_tecnico = t.id_tecnico
                """;
    }

    private OrdenMantenimiento mapRow(ResultSet resultSet) throws Exception {
        Equipo equipo = new Equipo(
                resultSet.getLong("id_equipo"),
                resultSet.getString("equipo_nombre"),
                TipoEquipo.valueOf(resultSet.getString("equipo_tipo")),
                resultSet.getString("equipo_marca"),
                resultSet.getString("equipo_modelo"),
                resultSet.getString("equipo_numero_serie"),
                resultSet.getString("equipo_ubicacion_planta"),
                resultSet.getDate("equipo_fecha_instalacion").toLocalDate(),
                resultSet.getString("equipo_estado_operativo"),
                Criticidad.valueOf(resultSet.getString("equipo_criticidad"))
        );

        Tecnico tecnico = new Tecnico(
                resultSet.getLong("id_tecnico"),
                resultSet.getString("tecnico_nombre_completo"),
                resultSet.getString("tecnico_rfc"),
                resultSet.getString("tecnico_telefono"),
                resultSet.getString("tecnico_correo"),
                TipoEquipo.valueOf(resultSet.getString("tecnico_especialidad")),
                NivelCertificacion.valueOf(resultSet.getString("tecnico_nivel_certificacion")),
                resultSet.getDate("tecnico_fecha_ingreso").toLocalDate(),
                EstatusTecnico.valueOf(resultSet.getString("tecnico_estatus"))
        );

        Date fechaInicio = resultSet.getDate("fecha_inicio");
        Date fechaCierre = resultSet.getDate("fecha_cierre");

        return new OrdenMantenimiento(
                resultSet.getLong("id_orden"),
                equipo,
                tecnico,
                resultSet.getString("tipo_mantenimiento"),
                resultSet.getDate("fecha_programada").toLocalDate(),
                fechaInicio == null ? null : fechaInicio.toLocalDate(),
                fechaCierre == null ? null : fechaCierre.toLocalDate(),
                resultSet.getString("descripcion_trabajo"),
                resultSet.getBigDecimal("costo_estimado"),
                resultSet.getBigDecimal("costo_real"),
                EstadoOrden.valueOf(resultSet.getString("estado_orden"))
        );
    }
}