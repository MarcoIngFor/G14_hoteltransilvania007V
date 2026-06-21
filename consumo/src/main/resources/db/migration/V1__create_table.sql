CREATE TABLE consumo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    id_servicio_extra BIGINT NOT NULL,
    id_reserva BIGINT NOT NULL,
    id_cliente BIGINT NOT NULL,
    id_habitacion BIGINT NOT NULL,

    cantidad INT NOT NULL,
    total_consumo DOUBLE NOT NULL,
    fecha DATE NOT NULL
);