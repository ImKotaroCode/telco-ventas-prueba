# 📡 Telco Ventas – Prueba Técnica Backend

Sistema de gestión de ventas para el producto **Telco Fija Hogar**, desarrollado con **Java 17 + Spring Boot 3 + JWT + PostgreSQL** y un frontend mínimo sin frameworks.

---

## 🎯 Flujo Implementado

1. **AGENTE** registra una venta (estado inicial: PENDIENTE).
2. **BACKOFFICE** aprueba o rechaza la venta (requiere motivo en caso de rechazo).
3. **SUPERVISOR** consulta ventas de su equipo con filtros y visualiza reportes.
4. **ADMIN** tiene acceso total.

---

## 🏗 Stack Tecnológico

### Backend
- Java 17
- Spring Boot 3
- Spring Security + JWT (stateless)
- Spring Data JPA
- PostgreSQL

### Frontend
- HTML
- CSS
- JavaScript (sin frameworks)

### Arquitectura en capas

Controller → Service → Repository → PostgreSQL

---

## 👥 Roles

| Rol          | Permisos |
|--------------|----------|
| AGENTE       | Crear ventas y ver solo sus ventas |
| BACKOFFICE   | Aprobar o rechazar ventas pendientes |
| SUPERVISOR   | Ver ventas del equipo y reportes |
| ADMIN        | Acceso total |

---

## 🔐 Autenticación

### Login

POST /api/v1/auth/login

Respuesta:

{
  "token": "JWT_TOKEN",
  "username": "agente1",
  "rol": "AGENTE"
}

Para endpoints protegidos se requiere el header:

Authorization: Bearer <token>

---

## 📦 Endpoints Principales

Prefijo base: /api/v1

### AGENTE
- POST /ventas
- GET /ventas/mis-ventas

### BACKOFFICE
- GET /ventas/pendientes
- POST /ventas/{id}/aprobar
- POST /ventas/{id}/rechazar

### SUPERVISOR
- GET /ventas/equipo
- GET /reportes/resumen

### El resumen incluye:
- Conteo por estado
- Monto total aprobado
- Serie de ventas por período (día o mes)

Filtros soportados:
- estado
- agenteId
- desde
- hasta
- paginación (page, size, sort, dir)

---

## 🗄 Base de Datos

Base de datos: telco

### Crear base de datos y cargar scripts

Windows (PowerShell):

createdb telco

psql -U postgres -d telco -f schema.sql

psql -U postgres -d telco -f data.sql

Linux / Mac:

createdb telco

psql -U postgres -d telco -f schema.sql

psql -U postgres -d telco -f data.sql

Incluye:
- Constraint único en codigo_llamada
- Foreign keys correctamente definidas
- Índices por estado, agente y fecha

---

## 👤 Usuarios Seed

| Usuario      | Password     | Rol |
|--------------|-------------|------|
| admin        | Admin*123   | ADMIN |
| agente1      | Agente*123  | AGENTE |
| back1        | Back*123    | BACKOFFICE |
| supervisor1  | Sup*123     | SUPERVISOR |

Incluye 5 ventas con estados:
- PENDIENTE
- APROBADA
- RECHAZADA (con motivo)

---

## ▶ Ejecutar Backend

Configurar:

backend/src/main/resources/application.properties

Ejemplo:

server.port=8080

spring.datasource.url=jdbc:postgresql://localhost:5432/telco

spring.datasource.username=postgres

spring.datasource.password=123456


spring.jpa.hibernate.ddl-auto=none

spring.sql.init.mode=always

app.jwt.secret=6dJ93kLx8vP2qWzR5tNyB7uMfGhK4cQa1XyZpE8r

app.jwt.expiration-minutes=240

Ejecutar desde la carpeta backend:

mvn spring-boot:run

Base URL:

http://localhost:8080/api/v1

---

## ▶ Ejecutar Frontend

Abrir:

frontend/index.html

O usar Live Server / Live Preview (microsoft).

Ejemplo:

http://127.0.0.1:3000

CORS configurado para:
- http://localhost:5173
- http://127.0.0.1:3000
- http://localhost:3000

---

## 📘 Documentación API (Swagger)

Una vez levantado el backend:

http://localhost:8080/swagger-ui/index.html

Desde Swagger se pueden probar todos los endpoints.

Para endpoints protegidos:
1. Ejecutar POST /api/v1/auth/login
2. Copiar el token
3. Click en Authorize
4. Pegar el token como Bearer

---

## 📘 OpenAPI

La especificación OpenAPI versionada en el repositorio se encuentra en:

docs/openapi.json

También puede visualizarse en tiempo real en:

http://localhost:8080/v3/api-docs

---

## 📁 Documentación Técnica

La carpeta docs/ incluye:
- Diagrama de arquitectura
- Decisiones técnicas
- Guía de despliegue
- OpenAPI exportado

---

## 🛡 Seguridad

- API stateless con JWT
- Control de acceso por rol
- Validaciones con Bean Validation
- Manejo global de excepciones con formato JSON consistente:

{
  "timestamp": "...",
  "path": "...",
  "error": "...",
  "message": "..."
}

---

## ✅ Estado del Proyecto

✔ Flujo completo funcional  
✔ Seguridad por roles  
✔ Validaciones implementadas  
✔ Reportes con filtros  
✔ Base de datos con datos seed  
✔ OpenAPI versionado  
✔ Documentación técnica incluida  

