CREATE TABLE pagos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_reserva BIGINT NOT NULL,
    subtotal_habitacion DOUBLE NOT NULL,
    subtotal_servicios DOUBLE NOT NULL,
    total_pagar DOUBLE NOT NULL,
    metodo_pago VARCHAR(50) NOT NULL,
    estado_pago VARCHAR(50) NOT NULL,
    fecha_pago DATE NOT NULL
);