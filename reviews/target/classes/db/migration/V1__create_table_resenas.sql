CREATE TABLE resenas (

    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    cliente_id BIGINT NOT NULL,

    habitacion_id BIGINT NOT NULL,

    comentario VARCHAR(500) NOT NULL,

    calificacion INT NOT NULL,

    fecha DATE
);