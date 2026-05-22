# SIGOMEI Servidor

Sistema Distribuido para la Gestión de Órdenes de Mantenimiento de Equipos Industriales.

Este proyecto implementa una arquitectura cliente-servidor en Java usando RMI.  
El servidor se conecta a MySQL mediante JDBC y el cliente consume únicamente servicios remotos RMI.

---

## Requisitos

- Java 17 o superior
- Maven 3.9+
- MySQL Server 8.x

---

## Configuración de la base de datos

El servidor utiliza MySQL como base de datos.

Antes de ejecutar el servidor, crear la base de datos ejecutando los scripts en este orden:

```text
database/01_schema.sql
database/02_data.sql
```

En MySQL Workbench se pueden abrir ambos archivos y ejecutarlos manualmente.

También se puede crear el usuario de base de datos con:

```sql
CREATE USER IF NOT EXISTS 'sigomei'@'localhost' IDENTIFIED BY 'sigomei123';
GRANT ALL PRIVILEGES ON sigomei_db.* TO 'sigomei'@'localhost';
FLUSH PRIVILEGES;
```

---

## Configuración del servidor

La conexión a la base de datos se configura en el archivo externo:

```text
config/server.properties
```

Contenido esperado:

```properties
rmi.host=127.0.0.1
rmi.registry.port=1099

rmi.service.equipo=EquipoService
rmi.service.tecnico=TecnicoService
rmi.service.orden=OrdenService

db.url=jdbc:mysql://localhost:3306/sigomei_db?useSSL=false&serverTimezone=America/Mexico_City&allowPublicKeyRetrieval=true
db.user=sigomei
db.password=sigomei123
db.driver=com.mysql.cj.jdbc.Driver
```

Las credenciales de la base de datos están en este archivo externo y no están embebidas en el código fuente.

---

## Configuración del cliente

El cliente utiliza el archivo:

```text
config/client.properties
```

Contenido esperado:

```properties
rmi.host=127.0.0.1
rmi.registry.port=1099

rmi.service.equipo=EquipoService
rmi.service.tecnico=TecnicoService
rmi.service.orden=OrdenService
```

El cliente no contiene datos de conexión a MySQL porque no se conecta directamente a la base de datos.  
Toda comunicación se realiza mediante servicios remotos RMI.

---

## Compilación

Desde la raíz del proyecto, donde se encuentra el archivo `pom.xml`, ejecutar:

```powershell
mvn clean compile
```

Después generar el classpath con las dependencias necesarias, incluyendo el driver JDBC de MySQL:

```powershell
mvn dependency:build-classpath "-Dmdep.outputFile=cp.txt"
```

---

## Ejecución del servidor

Antes de iniciar el servidor, verificar que MySQL esté encendido y que la base de datos `sigomei_db` exista.

Ejecutar:

```powershell
$cp = Get-Content cp.txt
java "-Dsigomei.config=config\server.properties" -cp "target\classes;$cp" mx.uv.sigomei.app.ServidorSigomeiApp
```

Si el servidor inicia correctamente, se mostrará una salida similar a:

```text
Servidor SIGOMEI iniciado correctamente.
Host RMI: 127.0.0.1
Puerto RMI Registry: 1099
Servicio publicado: EquipoService
Servicio publicado: TecnicoService
Servicio publicado: OrdenService
Servidor esperando clientes. Presiona Ctrl + C para detenerlo.
```

La terminal del servidor debe permanecer abierta mientras se ejecuta el cliente.

---

## Ejecución del cliente

Abrir otra terminal en la raíz del proyecto y ejecutar:

```powershell
$cp = Get-Content cp.txt
java "-Dsigomei.client.config=config\client.properties" -cp "target\classes;$cp" mx.uv.sigomei.app.ClienteSigomeiDemoApp
```

El cliente se conectará al servidor mediante RMI y ejecutará operaciones sobre equipos, técnicos y órdenes.

Una salida correcta debe verse similar a:

```text
Equipo registrado: EquipoDTO{...}
Tecnico registrado: TecnicoDTO{...}
Orden creada correctamente: OrdenMantenimientoDTO{...}
Regla rechazada correctamente: RN-02: el equipo ya tiene una orden activa en esa fecha
Orden actualizada a EN_EJECUCION: OrdenMantenimientoDTO{...}
```

---

## Ejecución de pruebas unitarias

Para ejecutar las pruebas:

```powershell
mvn test
```

Para guardar la salida de consola como evidencia:

```powershell
mvn test *> evidencia-verde.txt
```