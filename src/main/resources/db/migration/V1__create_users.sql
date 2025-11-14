CREATE TABLE IF NOT EXISTS users (
  id UUID PRIMARY KEY,
  nombres VARCHAR(150) NOT NULL,
  apellidos VARCHAR(150) NOT NULL,
  usuario VARCHAR(100) NOT NULL UNIQUE,
  password_hash VARCHAR(200) NOT NULL,
  fecha_creacion TIMESTAMP NOT NULL,
  fecha_actualizacion TIMESTAMP NOT NULL
);
