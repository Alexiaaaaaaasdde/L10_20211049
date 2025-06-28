CREATE DATABASE buscaminas;
USE buscaminas;

CREATE TABLE configuracion (
    id BIGINT PRIMARY KEY,
    filas INT NOT NULL,
    columnas INT NOT NULL,
    cantidad_bombas INT NOT NULL
);

CREATE TABLE posicionbomba (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fila INT NOT NULL,
    columna INT NOT NULL
);

CREATE TABLE movimiento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fila INT NOT NULL,
    columna INT NOT NULL,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Insertar configuración de 8x8 con 10 bombas
INSERT INTO configuracion (id, filas, columnas, cantidad_bombas)
VALUES (1, 8, 8, 10);

-- Insertar algunas bombas manualmente
INSERT INTO posicionbomba (fila, columna) VALUES
(0, 3), (1, 1), (2, 5), (3, 3), (4, 2),
(5, 6), (6, 1), (6, 7), (7, 4), (7, 7);