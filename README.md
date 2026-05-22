# SIGOMEI Servidor

Sistema Distribuido para la Gestión de Órdenes de Mantenimiento de Equipos Industriales.

Este proyecto implementa una arquitectura cliente-servidor en Java usando RMI.  
El cliente no se conecta directamente a la base de datos ni a los repositorios; consume servicios remotos publicados por el servidor.

## Requisitos

- Java 17 o superior
- Maven 3.9+
- Git
- Windows PowerShell, CMD o terminal equivalente

## Estructura principal

```text
src/main/java/mx/uv/sigomei/
├── app/              Clases principales para iniciar servidor y cliente demo
├── domain/           Entidades del dominio: Equipo, Técnico y Orden
├── dto/              Objetos de transferencia usados por RMI
├── enums/            Enumeraciones del sistema
├── exception/        Excepciones de negocio y validación
├── mapper/           Conversión entre entidades y DTOs
├── remote/           Interfaces remotas RMI
├── remote/impl/      Implementaciones remotas RMI
├── repository/       Interfaces de repositorio
├── repository/memory Repositorios temporales en memoria
├── service/          Interfaces de servicio
├── service/impl/     Implementaciones de la lógica de negocio
├── state/            Validación de transiciones de estado
└── validator/        Validación de reglas de negocio
```

## Compilar

```powershell
mvn clean compile
```
## Ejecutar pruebas
```powershell
mvn test
```

## Ejecutar servidor RMI
```powershell
java "-Dsigomei.config=config\server.properties" -cp "target\classes" mx.uv.sigomei.app.ServidorSigomeiApp
```

La terminal del servidor debe quedar abierta.

## Ejecutar cliente demo

En otra terminal:
```powershell
java "-Dsigomei.client.config=config\client.properties" -cp "target\classes" mx.uv.sigomei.app.ClienteSigomeiDemoApp
```