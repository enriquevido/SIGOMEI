DROP DATABASE IF EXISTS sigomei_db;
CREATE DATABASE sigomei_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE sigomei_db;

CREATE TABLE equipo (
    id_equipo BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    marca VARCHAR(100) NOT NULL,
    modelo VARCHAR(100) NOT NULL,
    numero_serie VARCHAR(100) NOT NULL UNIQUE,
    ubicacion_planta VARCHAR(150) NOT NULL,
    fecha_instalacion DATE NOT NULL,
    estado_operativo VARCHAR(50) NOT NULL,
    criticidad VARCHAR(20) NOT NULL,

    CONSTRAINT chk_equipo_tipo
        CHECK (tipo IN ('ELECTRICO', 'MECANICO', 'INSTRUMENTACION', 'HIDRAULICO')),

    CONSTRAINT chk_equipo_criticidad
        CHECK (criticidad IN ('BAJA', 'MEDIA', 'ALTA'))
);

CREATE TABLE tecnico (
    id_tecnico BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre_completo VARCHAR(150) NOT NULL,
    rfc VARCHAR(20) NOT NULL UNIQUE,
    telefono VARCHAR(10) NOT NULL,
    correo VARCHAR(150) NOT NULL,
    especialidad VARCHAR(30) NOT NULL,
    nivel_certificacion VARCHAR(5) NOT NULL,
    fecha_ingreso DATE NOT NULL,
    estatus VARCHAR(20) NOT NULL,

    CONSTRAINT chk_tecnico_especialidad
        CHECK (especialidad IN ('ELECTRICO', 'MECANICO', 'INSTRUMENTACION', 'HIDRAULICO')),

    CONSTRAINT chk_tecnico_nivel
        CHECK (nivel_certificacion IN ('I', 'II', 'III')),

    CONSTRAINT chk_tecnico_estatus
        CHECK (estatus IN ('ACTIVO', 'INACTIVO')),

    CONSTRAINT chk_tecnico_telefono
        CHECK (telefono REGEXP '^[0-9]{10}$')
);

CREATE TABLE orden_mantenimiento (
    id_orden BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_equipo BIGINT NOT NULL,
    id_tecnico BIGINT NOT NULL,
    tipo_mantenimiento VARCHAR(50) NOT NULL,
    fecha_programada DATE NOT NULL,
    fecha_inicio DATE NULL,
    fecha_cierre DATE NULL,
    descripcion_trabajo VARCHAR(255) NOT NULL,
    costo_estimado DECIMAL(10,2) NOT NULL,
    costo_real DECIMAL(10,2) NULL,
    estado_orden VARCHAR(30) NOT NULL,

    CONSTRAINT fk_orden_equipo
        FOREIGN KEY (id_equipo)
        REFERENCES equipo(id_equipo),

    CONSTRAINT fk_orden_tecnico
        FOREIGN KEY (id_tecnico)
        REFERENCES tecnico(id_tecnico),

    CONSTRAINT chk_orden_estado
        CHECK (estado_orden IN ('PROGRAMADA', 'EN_EJECUCION', 'FINALIZADA', 'CANCELADA')),

    CONSTRAINT chk_orden_tipo_mantenimiento
        CHECK (tipo_mantenimiento IN ('Preventivo', 'Correctivo')),

    CONSTRAINT chk_orden_costo_estimado
        CHECK (costo_estimado >= 0),

    CONSTRAINT chk_orden_costo_real
        CHECK (costo_real IS NULL OR costo_real >= 0),

    CONSTRAINT chk_orden_fechas
        CHECK (
            (fecha_inicio IS NULL OR fecha_inicio >= fecha_programada)
            AND
            (fecha_cierre IS NULL OR fecha_inicio IS NOT NULL)
            AND
            (fecha_cierre IS NULL OR fecha_cierre >= fecha_inicio)
        ),

    CONSTRAINT chk_orden_finalizada
        CHECK (
            (estado_orden = 'FINALIZADA' AND fecha_cierre IS NOT NULL AND costo_real IS NOT NULL)
            OR
            (estado_orden <> 'FINALIZADA' AND fecha_cierre IS NULL AND costo_real IS NULL)
        )
);

CREATE INDEX idx_orden_equipo ON orden_mantenimiento(id_equipo);
CREATE INDEX idx_orden_tecnico ON orden_mantenimiento(id_tecnico);
CREATE INDEX idx_orden_estado ON orden_mantenimiento(estado_orden);
CREATE INDEX idx_orden_fecha_programada ON orden_mantenimiento(fecha_programada);