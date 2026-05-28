package mx.uv.sigomei.util;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class AuditLogger {

    private static final Logger logger;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    static {
        logger = Logger.getLogger("sigomei.audit");
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
        try {
            new File("logs").mkdirs();
            FileHandler handler = new FileHandler("logs/sigomei-audit.log", true);
            handler.setLevel(Level.ALL);
            handler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    return LocalDateTime.now().format(FMT)
                            + " " + record.getLevel().getName()
                            + " [" + record.getSourceClassName() + "] "
                            + record.getMessage()
                            + System.lineSeparator();
                }
            });
            logger.addHandler(handler);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private AuditLogger() {}

    public static void log(Level nivel, String clase, String mensaje) {
        LogRecord record = new LogRecord(nivel, mensaje);
        record.setSourceClassName(clase);
        record.setSourceMethodName("");
        logger.log(record);
    }
}
