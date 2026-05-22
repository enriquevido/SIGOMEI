package mx.uv.sigomei.repository.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import mx.uv.sigomei.domain.Tecnico;
import mx.uv.sigomei.enums.EstatusTecnico;
import mx.uv.sigomei.enums.NivelCertificacion;
import mx.uv.sigomei.enums.TipoEquipo;
import mx.uv.sigomei.repository.TecnicoRepository;

public class JdbcTecnicoRepository implements TecnicoRepository {

    private final DatabaseConnectionProvider connectionProvider;

    public JdbcTecnicoRepository(DatabaseConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public Optional<Tecnico> findById(Long id) {
        String sql = """
                SELECT id_tecnico, nombre_completo, rfc, telefono, correo,
                       especialidad, nivel_certificacion, fecha_ingreso, estatus
                FROM tecnico
                WHERE id_tecnico = ?
                """;

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
            throw new IllegalStateException("Error al buscar tecnico por id: " + id, e);
        }
    }

    @Override
    public Tecnico save(Tecnico tecnico) {
        if (tecnico.getId() == null) {
            return insert(tecnico);
        }
        return update(tecnico);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM tecnico WHERE id_tecnico = ?";

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (Exception e) {
            throw new IllegalStateException("Error al eliminar tecnico con id: " + id, e);
        }
    }

    @Override
    public List<Tecnico> findAll() {
        String sql = """
                SELECT id_tecnico, nombre_completo, rfc, telefono, correo,
                       especialidad, nivel_certificacion, fecha_ingreso, estatus
                FROM tecnico
                ORDER BY id_tecnico
                """;

        List<Tecnico> tecnicos = new ArrayList<>();

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                tecnicos.add(mapRow(resultSet));
            }

            return tecnicos;

        } catch (Exception e) {
            throw new IllegalStateException("Error al listar tecnicos", e);
        }
    }

    private Tecnico insert(Tecnico tecnico) {
        String sql = """
                INSERT INTO tecnico (
                    nombre_completo, rfc, telefono, correo,
                    especialidad, nivel_certificacion, fecha_ingreso, estatus
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            fillStatement(statement, tecnico);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    tecnico.setId(keys.getLong(1));
                }
            }

            return tecnico;

        } catch (Exception e) {
            throw new IllegalStateException("Error al insertar tecnico", e);
        }
    }

    private Tecnico update(Tecnico tecnico) {
        String sql = """
                UPDATE tecnico
                SET nombre_completo = ?,
                    rfc = ?,
                    telefono = ?,
                    correo = ?,
                    especialidad = ?,
                    nivel_certificacion = ?,
                    fecha_ingreso = ?,
                    estatus = ?
                WHERE id_tecnico = ?
                """;

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            fillStatement(statement, tecnico);
            statement.setLong(9, tecnico.getId());
            statement.executeUpdate();

            return tecnico;

        } catch (Exception e) {
            throw new IllegalStateException("Error al actualizar tecnico con id: " + tecnico.getId(), e);
        }
    }

    private void fillStatement(PreparedStatement statement, Tecnico tecnico) throws Exception {
        statement.setString(1, tecnico.getNombreCompleto());
        statement.setString(2, tecnico.getRfc());
        statement.setString(3, tecnico.getTelefono());
        statement.setString(4, tecnico.getCorreo());
        statement.setString(5, tecnico.getEspecialidad().name());
        statement.setString(6, tecnico.getNivelCertificacion().name());
        statement.setDate(7, Date.valueOf(tecnico.getFechaIngreso()));
        statement.setString(8, tecnico.getEstatus().name());
    }

    private Tecnico mapRow(ResultSet resultSet) throws Exception {
        return new Tecnico(
                resultSet.getLong("id_tecnico"),
                resultSet.getString("nombre_completo"),
                resultSet.getString("rfc"),
                resultSet.getString("telefono"),
                resultSet.getString("correo"),
                TipoEquipo.valueOf(resultSet.getString("especialidad")),
                NivelCertificacion.valueOf(resultSet.getString("nivel_certificacion")),
                resultSet.getDate("fecha_ingreso").toLocalDate(),
                EstatusTecnico.valueOf(resultSet.getString("estatus"))
        );
    }
}