package mx.uv.sigomei.app;

import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

import mx.uv.sigomei.remote.impl.AuthRemoteService;
import mx.uv.sigomei.remote.impl.EquipoRemoteService;
import mx.uv.sigomei.remote.impl.OrdenRemoteService;
import mx.uv.sigomei.remote.impl.TecnicoRemoteService;

public class ServidorSigomeiApp {

    public static void main(String[] args) throws Exception {
        String configPath = System.getProperty("sigomei.config", "config/server.properties");

        Properties properties = PropertiesLoader.load(configPath);

        String host = PropertiesLoader.getString(properties, "rmi.host", "localhost");
        int port = PropertiesLoader.getInt(properties, "rmi.registry.port", 1099);
        int servicePort = PropertiesLoader.getInt(properties, "rmi.service.port", 1100);

        String equipoName = PropertiesLoader.getString(properties, "rmi.service.equipo", "EquipoService");
        String tecnicoName = PropertiesLoader.getString(properties, "rmi.service.tecnico", "TecnicoService");
        String ordenName = PropertiesLoader.getString(properties, "rmi.service.orden", "OrdenService");
        String authName = PropertiesLoader.getString(properties, "rmi.service.auth", "AuthService");
        boolean sslEnabled = Boolean.parseBoolean(PropertiesLoader.getString(properties, "rmi.ssl.enabled", "false"));

        System.setProperty("java.rmi.server.hostname", host);
        configurarSsl(properties, sslEnabled);

        Registry registry = crearRegistry(port, sslEnabled);

        SigomeiContext context = new SigomeiContext();

        EquipoRemoteService equipoRemoteService =
                new EquipoRemoteService(context.equipoService(), servicePort, sslEnabled);

        TecnicoRemoteService tecnicoRemoteService =
                new TecnicoRemoteService(context.tecnicoService(), servicePort, sslEnabled);

        OrdenRemoteService ordenRemoteService =
                new OrdenRemoteService(
                        context.ordenService(),
                        context.equipoService(),
                        context.tecnicoService(),
                        servicePort,
                        sslEnabled
                );

        AuthRemoteService authRemoteService = new AuthRemoteService(context.authService(), servicePort, sslEnabled);

        registry.rebind(equipoName, equipoRemoteService);
        registry.rebind(tecnicoName, tecnicoRemoteService);
        registry.rebind(ordenName, ordenRemoteService);
        registry.rebind(authName, authRemoteService);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            context.cerrar();
            System.out.println("Servidor SIGOMEI detenido.");
        }));

        System.out.println("Servidor SIGOMEI iniciado correctamente.");
        System.out.println("Host RMI: " + host);
        System.out.println("Puerto RMI Registry: " + port);
        System.out.println("Puerto servicios RMI: " + servicePort);
        System.out.println("SSL/TLS: " + (sslEnabled ? "habilitado" : "deshabilitado"));
        System.out.println("Servicio publicado: " + equipoName);
        System.out.println("Servicio publicado: " + tecnicoName);
        System.out.println("Servicio publicado: " + ordenName);
        System.out.println("Servicio publicado: " + authName);
        System.out.println("Servidor esperando clientes. Presiona Ctrl + C para detenerlo.");

        new CountDownLatch(1).await();
    }

    private static Registry crearRegistry(int port, boolean sslEnabled) throws Exception {
        if (!sslEnabled) {
            return LocateRegistry.createRegistry(port);
        }

        RMIClientSocketFactory csf = new SslRMIClientSocketFactory();
        RMIServerSocketFactory ssf = new SslRMIServerSocketFactory(null, null, false);
        return LocateRegistry.createRegistry(port, csf, ssf);
    }

    private static void configurarSsl(Properties properties, boolean sslEnabled) {
        if (!sslEnabled) {
            return;
        }

        System.setProperty("javax.net.ssl.keyStore",
                PropertiesLoader.getString(properties, "rmi.keystore.path", "config/server.jks"));
        System.setProperty("javax.net.ssl.keyStorePassword",
                PropertiesLoader.getString(properties, "rmi.keystore.password", ""));
        System.setProperty("javax.net.ssl.trustStore",
                PropertiesLoader.getString(properties, "rmi.truststore.path", "config/client.jks"));
        System.setProperty("javax.net.ssl.trustStorePassword",
                PropertiesLoader.getString(properties, "rmi.truststore.password", ""));
    }
}
