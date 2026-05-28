package mx.uv.sigomei.repository.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

public class JdbcUsuarioRepository {

    private final DatabaseConnectionProvider connectionProvider;

    public JdbcUsuarioRepository(DatabaseConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public Optional<UsuarioRow> findByUsername(String username) {
        String sql = """
                SELECT id_usuario, username, password_hash, rol, intentos_fallidos, bloqueado_hasta
                FROM usuario
                WHERE username = ?
                """;

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error al buscar usuario: " + username, e);
        }
    }

    public void actualizarIntentos(String username, int intentos, LocalDateTime bloqueadoHasta) {
        String sql = """
                UPDATE usuario
                SET intentos_fallidos = ?,
                    bloqueado_hasta = ?
                WHERE username = ?
                """;

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, intentos);
            statement.setTimestamp(2, bloqueadoHasta == null ? null : Timestamp.valueOf(bloqueadoHasta));
            statement.setString(3, username);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new IllegalStateException("Error al actualizar intentos del usuario: " + username, e);
        }
    }

    public void resetearIntentos(String username) {
        actualizarIntentos(username, 0, null);
    }

    private UsuarioRow mapRow(ResultSet resultSet) throws Exception {
        Timestamp bloqueadoHasta = resultSet.getTimestamp("bloqueado_hasta");
        return new UsuarioRow(
                resultSet.getLong("id_usuario"),
                resultSet.getString("username"),
                resultSet.getString("password_hash"),
                resultSet.getString("rol"),
                resultSet.getInt("intentos_fallidos"),
                bloqueadoHasta == null ? null : bloqueadoHasta.toLocalDateTime()
        );
    }

    public record UsuarioRow(
            Long idUsuario,
            String username,
            String passwordHash,
            String rol,
            int intentosFallidos,
            LocalDateTime bloqueadoHasta
    ) {
    }
}
