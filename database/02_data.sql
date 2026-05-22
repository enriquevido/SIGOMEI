USE sigomei_db;

INSERT INTO equipo (
    id_equipo,
    nombre,
    tipo,
    marca,
    modelo,
    numero_serie,
    ubicacion_planta,
    fecha_instalacion,
    estado_operativo,
    criticidad
) VALUES
(
    1,
    'Compresor Atlas',
    'MECANICO',
    'Atlas Copco',
    'GA55',
    'SN-COMP-001',
    'Planta Norte',
    '2022-03-15',
    'Operativo',
    'ALTA'
),
(
    2,
    'Tablero SIEMENS',
    'ELECTRICO',
    'Siemens',
    'S7-1200',
    'SN-ELEC-002',
    'Planta Sur',
    '2021-08-20',
    'Operativo',
    'MEDIA'
),
(
    3,
    'Sensor PT-100',
    'INSTRUMENTACION',
    'Wika',
    'PT100',
    'SN-INST-003',
    'Laboratorio',
    '2023-02-10',
    'Operativo',
    'BAJA'
),
(
    4,
    'Bomba Centrifuga',
    'HIDRAULICO',
    'Grundfos',
    'CR-10',
    'SN-HIDR-004',
    'Planta Norte',
    '2020-11-05',
    'En falla',
    'ALTA'
);

INSERT INTO tecnico (
    id_tecnico,
    nombre_completo,
    rfc,
    telefono,
    correo,
    especialidad,
    nivel_certificacion,
    fecha_ingreso,
    estatus
) VALUES
(
    1,
    'Lopez Hernandez Juan',
    'LOHJ800101',
    '9211234567',
    'juan@empresa.mx',
    'MECANICO',
    'III',
    '2020-01-10',
    'ACTIVO'
),
(
    2,
    'Perez Garcia Ana',
    'PEGA900215',
    '9217654321',
    'ana@empresa.mx',
    'ELECTRICO',
    'II',
    '2021-04-18',
    'ACTIVO'
),
(
    3,
    'Ramirez Torres Luis',
    'RATL851120',
    '9211112233',
    'luis@empresa.mx',
    'HIDRAULICO',
    'I',
    '2019-09-12',
    'ACTIVO'
),
(
    4,
    'Flores Diaz Carmen',
    'FODC750630',
    '9212223344',
    'carmen@empresa.mx',
    'MECANICO',
    'II',
    '2018-06-25',
    'INACTIVO'
);

INSERT INTO orden_mantenimiento (
    id_orden,
    id_equipo,
    id_tecnico,
    tipo_mantenimiento,
    fecha_programada,
    fecha_inicio,
    fecha_cierre,
    descripcion_trabajo,
    costo_estimado,
    costo_real,
    estado_orden
) VALUES
(
    1,
    1,
    1,
    'Preventivo',
    '2026-06-01',
    NULL,
    NULL,
    'Cambio de aceite y revision general',
    3500.00,
    NULL,
    'PROGRAMADA'
),
(
    2,
    2,
    2,
    'Correctivo',
    '2026-06-02',
    '2026-06-02',
    NULL,
    'Revision de tablero electrico',
    4200.00,
    NULL,
    'EN_EJECUCION'
),
(
    3,
    3,
    2,
    'Preventivo',
    '2026-05-20',
    '2026-05-20',
    '2026-05-21',
    'Calibracion de sensor',
    1800.00,
    1750.00,
    'FINALIZADA'
),
(
    4,
    4,
    3,
    'Correctivo',
    '2026-06-10',
    NULL,
    NULL,
    'Inspeccion inicial de bomba',
    5000.00,
    NULL,
    'PROGRAMADA'
);