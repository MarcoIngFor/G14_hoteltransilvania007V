INSERT INTO tiposervicio (id, nombre) VALUES 
(1, 'SPA'), 
(2, 'ALIMENTACION'), 
(3, 'RECREACION');

-- 3. INSERTAR SERVICIOS DE SPA (ID_TIPOSERVICIO = 1)
INSERT INTO servicoextra (nombre, descripcion, precio, id_tiposervicio) VALUES
('Servicio de Spa', 'Acceso completo a circuito de aguas y vapor', 25000.0, 1),
('Masaje Descontracturante', 'Sesion de 60 min con aceites esenciales', 35000.0, 1),
('Sauna', 'Sesion de calor seco para desintoxicacion', 12000.0, 1),
('Banera / Tina de Hidromasaje', 'Bano privado con sales relajantes', 18000.0, 1),
('Salon de Belleza', 'Servicio de manicura y pedicura basica', 15000.0, 1),
('Peluqueria', 'Corte de cabello y peinado', 20000.0, 1),
('Gimnasio', 'Pase diario a maquinas y pesas', 8000.0, 1);

-- 4. INSERTAR SERVICIOS DE ALIMENTACION (ID_TIPOSERVICIO = 2)
INSERT INTO servicoextra (nombre, descripcion, precio, id_tiposervicio) VALUES
('Bar', 'Consumo de tragos y cocteleria', 15000.0, 2),
('Desayuno', 'Desayuno buffet o a la carta', 8500.0, 2),
('Cafeteria', 'Cafe de especialidad y pasteleria', 5000.0, 2),
('Bar en area de piscina', 'Bebidas y snacks en zona humeda', 15000.0, 2),
('Restaurante', 'Almuerzo o cena a la carta', 25000.0, 2);

-- 5. INSERTAR SERVICIOS DE RECREACION (ID_TIPOSERVICIO = 3)
INSERT INTO servicoextra (nombre, descripcion, precio, id_tiposervicio) VALUES
('Parque acuatico', 'Acceso a toboganes y juegos', 15000.0, 3),
('Mesa de pool', 'Alquiler por hora', 5000.0, 3),
('Discoteca', 'Entrada con cover incluido', 10000.0, 3),
('Programa para ninos', 'Cuidado y juegos dirigidos', 12000.0, 3),
('Fiestas tematicas', 'Acceso a show y cena especial', 20000.0, 3);