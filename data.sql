-- USUARIOS SEED

INSERT INTO usuarios (id, username, password_hash, rol, supervisor_id, activo)
VALUES
    (1, 'admin',
     '$2a$10$bSEKbZ3PJY6Oby3dDGHcVeflsxYxvJ5iDPADusKjA1M9LLfFnn.LK',
     'ADMIN', NULL, TRUE),

    (2, 'supervisor1',
     '$2a$10$G9uQUsoj55TxS5EyRAVKz.pF0p97yF7NLkR3lxCUqzgIMolQkwmKW',
     'SUPERVISOR', NULL, TRUE),

    (3, 'agente1',
     '$2a$10$A/R3u4prX/r32BV0j/nJKukgCU.bs4csDVolIDy6BxFA/wax1qUMu',
     'AGENTE', 2, TRUE),

    (4, 'back1',
     '$2a$10$fDDAeJEnls.r/0ZVrp569.XKwCYg5EBlS/so5jYGP6dpW/Aq0XiOm',
     'BACKOFFICE', NULL, TRUE);



-- VENTAS SEED

INSERT INTO ventas
(agente_id, dni_cliente, nombre_cliente, telefono_cliente, direccion_cliente,
 plan_actual, plan_nuevo, codigo_llamada, producto, monto, estado,
 motivo_rechazo, fecha_registro, fecha_validacion)
VALUES

-- PENDIENTE
(3, '12345678', 'Juan Perez', '999888777', 'Av. Lima 123',
 'Plan A', 'Plan B', 'CALL-0001', 'FIJA_HOGAR', 120.50,
 'PENDIENTE', NULL,
 NOW() - INTERVAL '2 days', NULL),

-- APROBADA
(3, '87654321', 'Maria Diaz', '987654321', 'Jr. Arequipa 456',
 'Plan Básico', 'Plan Premium', 'CALL-0002', 'FIJA_HOGAR', 150.00,
 'APROBADA', NULL,
 NOW() - INTERVAL '5 days', NOW() - INTERVAL '4 days'),

-- RECHAZADA
(3, '10456789012', 'Empresa SAC', '912345678', 'Calle Comercio 789',
 'Plan X', 'Plan Y', 'CALL-0003', 'FIJA_HOGAR', 200.00,
 'RECHAZADA', 'Datos inválidos del cliente',
 NOW() - INTERVAL '7 days', NOW() - INTERVAL '6 days'),

-- PENDIENTE
(3, '11112222', 'Carlos Ruiz', '955444333', 'Av. Los Olivos 321',
 'Plan A', 'Plan C', 'CALL-0004', 'FIJA_HOGAR', 99.90,
 'PENDIENTE', NULL,
 NOW() - INTERVAL '1 day', NULL),

-- APROBADA
(3, '33334444', 'Lucia Torres', '988777666', 'Jr. Miraflores 555',
 'Plan Start', 'Plan Plus', 'CALL-0005', 'FIJA_HOGAR', 180.00,
 'APROBADA', NULL,
 NOW() - INTERVAL '10 days', NOW() - INTERVAL '9 days');