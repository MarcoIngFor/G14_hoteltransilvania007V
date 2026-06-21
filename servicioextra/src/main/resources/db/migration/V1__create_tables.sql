-- 1. Tabla Independiente: Catálogo de Tipos (SPA, ALIMENTACION, etc.)
CREATE TABLE tiposervicio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

-- 2. Tabla de Servicios: Depende de tiposervicio
CREATE TABLE servicoextra (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    precio DOUBLE NOT NULL,
    id_tiposervicio BIGINT NOT NULL,
    CONSTRAINT fk_servicio_tipo FOREIGN KEY (id_tiposervicio) REFERENCES tiposervicio(id)
);