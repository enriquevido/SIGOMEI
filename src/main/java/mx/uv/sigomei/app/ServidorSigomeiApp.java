package mx.uv.sigomei.app;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

import mx.uv.sigomei.remote.impl.EquipoRemoteService;
import mx.uv.sigomei.remote.impl.OrdenRemoteService;
import mx.uv.sigomei.remote.impl.TecnicoRemoteService;

public class ServidorSigomeiApp {

    public static void main(String[] args) throws Exception {
        String configPath = System.getProperty("sigomei.config", "config/server.properties");

        Properties properties = PropertiesLoader.load(configPath);

        String host = PropertiesLoader.getString(properties, "rmi.host", "localhost");
        int port = PropertiesLoader.getInt(properties, "rmi.registry.port", 1099);

        String equipoName = PropertiesLoader.getString(properties, "rmi.service.equipo", "EquipoService");
        String tecnicoName = PropertiesLoader.getString(properties, "rmi.service.tecnico", "TecnicoService");
        String ordenName = PropertiesLoader.getString(properties, "rmi.service.orden", "OrdenService");

        System.setProperty("java.rmi.server.hostname", host);

        Registry registry = LocateRegistry.createRegistry(port);

        SigomeiContext context = new SigomeiContext();

        EquipoRemoteService equipoRemoteService =
                new EquipoRemoteService(context.equipoService());

        TecnicoRemoteService tecnicoRemoteService =
                new TecnicoRemoteService(context.tecnicoService());

        OrdenRemoteService ordenRemoteService =
                new OrdenRemoteService(
                        context.ordenService(),
                        context.equipoService(),
                        context.tecnicoService()
                );

        registry.rebind(equipoName, equipoRemoteService);
        registry.rebind(tecnicoName, tecnicoRemoteService);
        registry.rebind(ordenName, ordenRemoteService);

        System.out.println("Servidor SIGOMEI iniciado correctamente.");
        System.out.println("Host RMI: " + host);
        System.out.println("Puerto RMI Registry: " + port);
        System.out.println("Servicio publicado: " + equipoName);
        System.out.println("Servicio publicado: " + tecnicoName);
        System.out.println("Servicio publicado: " + ordenName);
    }
}