package mx.uv.sigomei.repository.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import mx.uv.sigomei.app.PropertiesLoader;

public class DatabaseConnectionProvider {

    private final String url;
    private final String user;
    private final String password;

    public DatabaseConnectionProvider(String configPath) {
        Properties properties = PropertiesLoader.load(configPath);

        String driver = PropertiesLoader.getString(properties, "db.driver", "com.mysql.cj.jdbc.Driver");
        this.url = PropertiesLoader.getString(properties, "db.url", "");
        this.user = PropertiesLoader.getString(properties, "db.user", "");
        this.password = PropertiesLoader.getString(properties, "db.password", "");

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("No se encontro el driver JDBC: " + driver, e);
        }
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo abrir conexion a la base de datos", e);
        }
    }
}