CREATE TABLE usuarios_sesiones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    username VARCHAR(100) NOT NULL,
    fecha_login DATETIME NOT NULL,
    activo BOOLEAN NOT NULL
);