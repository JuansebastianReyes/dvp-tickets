CREATE TABLE IF NOT EXISTS tickets (
  id UUID PRIMARY KEY,
  descripcion VARCHAR(500) NOT NULL,
  usuario_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  fecha_creacion TIMESTAMP NOT NULL,
  fecha_actualizacion TIMESTAMP NOT NULL,
  status VARCHAR(20) NOT NULL,
  CONSTRAINT tickets_status_chk CHECK (status IN ('ABIERTO','CERRADO'))
);

CREATE INDEX IF NOT EXISTS idx_tickets_usuario ON tickets(usuario_id);
