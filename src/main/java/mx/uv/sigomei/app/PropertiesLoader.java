package mx.uv.sigomei.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesLoader {

    private PropertiesLoader() {
    }

    public static Properties load(String path) {
        Properties properties = new Properties();

        try (InputStream input = new FileInputStream(path)) {
            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo cargar el archivo de configuracion: " + path, e);
        }
    }

    public static int getInt(Properties properties, String key, int defaultValue) {
        String value = properties.getProperty(key);

        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return Integer.parseInt(value.trim());
    }

    public static String getString(Properties properties, String key, String defaultValue) {
        String value = properties.getProperty(key);

        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return value.trim();
    }
}