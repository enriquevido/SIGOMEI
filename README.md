# SIGOMEI Servidor

Sistema Distribuido para la Gestión de Órdenes de Mantenimiento de Equipos Industriales.

Este proyecto implementa el servidor Java RMI de SIGOMEI.  
El servidor se conecta a MySQL mediante JDBC y expone servicios remotos para el cliente JavaFX ubicado en `../sigomei-cliente`.

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

La conexión RMI y la base de datos se configuran en `config/server.properties`.

```properties
rmi.host=127.0.0.1
rmi.registry.port=1099
rmi.service.port=1100

rmi.service.equipo=EquipoService
rmi.service.tecnico=TecnicoService
rmi.service.orden=OrdenService
rmi.service.auth=AuthService

rmi.ssl.enabled=true
rmi.keystore.path=config/server.jks
rmi.keystore.password=sigomei123
rmi.truststore.path=config/client.jks
rmi.truststore.password=sigomei123

db.url=jdbc:mysql://localhost:3306/sigomei_db?useSSL=false&serverTimezone=America/Mexico_City&allowPublicKeyRetrieval=true
db.user=sigomei
db.password=sigomei123
db.driver=com.mysql.cj.jdbc.Driver
```

Las credenciales de la base de datos están en este archivo externo y no están embebidas en el código fuente.

---

## Compilación

Desde la raíz del proyecto, donde se encuentra `pom.xml`, ejecutar:

```powershell
mvn clean compile
```

Después generar el classpath con las dependencias necesarias:

```powershell
mvn dependency:build-classpath "-Dmdep.outputFile=cp.txt"
```

---

## Ejecución para demo con dos clientes en red

### En la máquina SERVIDOR

1. Ejecutar el script de generación de keystores (solo primera vez):

   ```bash
   bash scripts/generar-keystores.sh
   ```

2. Copiar `config/client.jks` a cada máquina cliente.

3. Verificar que `config/server.properties` tenga:

   ```properties
   rmi.host=<IP_LAN_DEL_SERVIDOR>
   rmi.registry.port=1099
   rmi.service.port=1100
   rmi.ssl.enabled=true
   ```

4. Abrir puertos 1099 y 1100 en firewall (Windows: regla de entrada TCP).

5. Iniciar el servidor:

   ```powershell
   $cp = Get-Content cp.txt
   java "-Dsigomei.config=config\server.properties" -cp "target\classes;$cp" mx.uv.sigomei.app.ServidorSigomeiApp
   ```

Salida esperada:

```text
Servidor SIGOMEI iniciado correctamente.
Host RMI: <IP configurada>
Puerto RMI Registry: 1099
Puerto servicios RMI: 1100
SSL/TLS: habilitado
Servicio publicado: EquipoService
Servicio publicado: TecnicoService
Servicio publicado: OrdenService
Servicio publicado: AuthService
Servidor esperando clientes. Presiona Ctrl + C para detenerlo.
```

### En cada máquina CLIENTE

1. Usar el módulo JavaFX `sigomei-cliente` y colocar ahí el `client.jks` copiado del servidor si SSL está habilitado.

2. Editar `sigomei-cliente/config/client.properties`:

   ```properties
   rmi.host=<IP_LAN_DEL_SERVIDOR>
   rmi.registry.port=1099
   rmi.ssl.enabled=true
   rmi.truststore.path=config/client.jks
   rmi.truststore.password=sigomei123
   ```

3. Ejecutar:

   ```powershell
   cd ..\sigomei-cliente
   mvn javafx:run -Dsigomei.client.config=config/client.properties
   ```

4. Los dos clientes pueden ejecutarse simultáneamente desde terminales distintas o máquinas distintas.

Para pruebas locales sin keystores, configurar `rmi.ssl.enabled=false` en servidor y cliente.

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
