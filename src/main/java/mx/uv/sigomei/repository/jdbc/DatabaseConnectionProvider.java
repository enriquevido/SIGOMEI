package mx.uv.sigomei.repository.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import mx.uv.sigomei.app.PropertiesLoader;

public class DatabaseConnectionProvider {

    private final HikariDataSource dataSource;

    public DatabaseConnectionProvider(String configPath) {
        Properties properties = PropertiesLoader.load(configPath);

        String driver = PropertiesLoader.getString(properties, "db.driver", "com.mysql.cj.jdbc.Driver");
        String url = PropertiesLoader.getString(properties, "db.url", "");
        String user = PropertiesLoader.getString(properties, "db.user", "");
        String password = PropertiesLoader.getString(properties, "db.password", "");

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("No se encontro el driver JDBC: " + driver, e);
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(20);
        config.setConnectionTimeout(3000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setPoolName("SIGOMEI-Pool");

        this.dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo abrir conexion a la base de datos", e);
        }
    }

    public void close() {
        dataSource.close();
    }
}
