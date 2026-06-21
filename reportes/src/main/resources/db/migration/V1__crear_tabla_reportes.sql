CREATE TABLE reportes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    fecha_generacion DATE NOT NULL,
    total_reservas INT NOT NULL,
    total_ingresos DOUBLE NOT NULL
);
