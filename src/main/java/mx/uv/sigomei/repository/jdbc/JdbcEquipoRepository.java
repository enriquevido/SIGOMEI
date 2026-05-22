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
import mx.uv.sigomei.enums.Criticidad;
import mx.uv.sigomei.enums.TipoEquipo;
import mx.uv.sigomei.repository.EquipoRepository;

public class JdbcEquipoRepository implements EquipoRepository {

    private final DatabaseConnectionProvider connectionProvider;

    public JdbcEquipoRepository(DatabaseConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public Optional<Equipo> findById(Long id) {
        String sql = """
                SELECT id_equipo, nombre, tipo, marca, modelo, numero_serie,
                       ubicacion_planta, fecha_instalacion, estado_operativo, criticidad
                FROM equipo
                WHERE id_equipo = ?
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
            throw new IllegalStateException("Error al buscar equipo por id: " + id, e);
        }
    }

    @Override
    public Equipo save(Equipo equipo) {
        if (equipo.getId() == null) {
            return insert(equipo);
        }
        return update(equipo);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM equipo WHERE id_equipo = ?";

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (Exception e) {
            throw new IllegalStateException("Error al eliminar equipo con id: " + id, e);
        }
    }

    @Override
    public List<Equipo> findAll() {
        String sql = """
                SELECT id_equipo, nombre, tipo, marca, modelo, numero_serie,
                       ubicacion_planta, fecha_instalacion, estado_operativo, criticidad
                FROM equipo
                ORDER BY id_equipo
                """;

        List<Equipo> equipos = new ArrayList<>();

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                equipos.add(mapRow(resultSet));
            }

            return equipos;

        } catch (Exception e) {
            throw new IllegalStateException("Error al listar equipos", e);
        }
    }

    private Equipo insert(Equipo equipo) {
        String sql = """
                INSERT INTO equipo (
                    nombre, tipo, marca, modelo, numero_serie,
                    ubicacion_planta, fecha_instalacion, estado_operativo, criticidad
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            fillStatement(statement, equipo);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    equipo.setId(keys.getLong(1));
                }
            }

            return equipo;

        } catch (Exception e) {
            throw new IllegalStateException("Error al insertar equipo", e);
        }
    }

    private Equipo update(Equipo equipo) {
        String sql = """
                UPDATE equipo
                SET nombre = ?,
                    tipo = ?,
                    marca = ?,
                    modelo = ?,
                    numero_serie = ?,
                    ubicacion_planta = ?,
                    fecha_instalacion = ?,
                    estado_operativo = ?,
                    criticidad = ?
                WHERE id_equipo = ?
                """;

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            fillStatement(statement, equipo);
            statement.setLong(10, equipo.getId());
            statement.executeUpdate();

            return equipo;

        } catch (Exception e) {
            throw new IllegalStateException("Error al actualizar equipo con id: " + equipo.getId(), e);
        }
    }

    private void fillStatement(PreparedStatement statement, Equipo equipo) throws Exception {
        statement.setString(1, equipo.getNombre());
        statement.setString(2, equipo.getTipo().name());
        statement.setString(3, equipo.getMarca());
        statement.setString(4, equipo.getModelo());
        statement.setString(5, equipo.getNumeroSerie());
        statement.setString(6, equipo.getUbicacionPlanta());
        statement.setDate(7, Date.valueOf(equipo.getFechaInstalacion()));
        statement.setString(8, equipo.getEstadoOperativo());
        statement.setString(9, equipo.getCriticidad().name());
    }

    private Equipo mapRow(ResultSet resultSet) throws Exception {
        return new Equipo(
                resultSet.getLong("id_equipo"),
                resultSet.getString("nombre"),
                TipoEquipo.valueOf(resultSet.getString("tipo")),
                resultSet.getString("marca"),
                resultSet.getString("modelo"),
                resultSet.getString("numero_serie"),
                resultSet.getString("ubicacion_planta"),
                resultSet.getDate("fecha_instalacion").toLocalDate(),
                resultSet.getString("estado_operativo"),
                Criticidad.valueOf(resultSet.getString("criticidad"))
        );
    }
}