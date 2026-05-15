CREATE TABLE habitacion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero INT NOT NULL UNIQUE,
    tipo VARCHAR(100) NOT NULL,
    precio_por_noche DOUBLE NOT NULL,
    disponible BOOLEAN NOT NULL DEFAULT TRUE
);