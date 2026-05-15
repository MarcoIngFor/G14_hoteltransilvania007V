CREATE TABLE checkin_checkout (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    reserva_id BIGINT NOT NULL,
    habitacion_id BIGINT NOT NULL,
    fecha_checkin DATETIME,
    fecha_checkout DATETIME,
    estado VARCHAR(50) NOT NULL
);