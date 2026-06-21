CREATE TABLE notificaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cliente BIGINT NOT NULL,
    destinatario VARCHAR(150) NOT NULL,
    canal VARCHAR(50) NOT NULL,
    mensaje VARCHAR(255) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    fecha_creacion DATETIME NOT NULL
);
