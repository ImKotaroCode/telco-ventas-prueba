CREATE TABLE usuarios (
                          id BIGSERIAL PRIMARY KEY,
                          username VARCHAR(60) NOT NULL UNIQUE,
                          password_hash VARCHAR(100) NOT NULL,
                          rol VARCHAR(20) NOT NULL,
                          supervisor_id BIGINT NULL,
                          activo BOOLEAN NOT NULL DEFAULT TRUE,
                          created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                          updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                          CONSTRAINT fk_supervisor FOREIGN KEY (supervisor_id) REFERENCES usuarios(id)
);

CREATE TABLE ventas (
                        id BIGSERIAL PRIMARY KEY,
                        agente_id BIGINT NOT NULL,
                        dni_cliente VARCHAR(11) NOT NULL,
                        nombre_cliente VARCHAR(120) NOT NULL,
                        telefono_cliente VARCHAR(9) NOT NULL,
                        direccion_cliente VARCHAR(200) NOT NULL,
                        plan_actual VARCHAR(80) NOT NULL,
                        plan_nuevo VARCHAR(80) NOT NULL,
                        codigo_llamada VARCHAR(60) NOT NULL UNIQUE,
                        producto VARCHAR(30) NOT NULL,
                        monto NUMERIC(12,2) NOT NULL,
                        estado VARCHAR(20) NOT NULL,
                        motivo_rechazo VARCHAR(250),
                        fecha_registro TIMESTAMP NOT NULL,
                        fecha_validacion TIMESTAMP,
                        created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                        updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                        CONSTRAINT fk_agente FOREIGN KEY (agente_id) REFERENCES usuarios(id)
);

CREATE INDEX idx_ventas_estado ON ventas(estado);
CREATE INDEX idx_ventas_agente ON ventas(agente_id);
CREATE INDEX idx_ventas_fecha_registro ON ventas(fecha_registro);