# 📡 Telco Ventas – Prueba Técnica Backend

Sistema de gestión de ventas para el producto **Telco Fija Hogar**, desarrollado con **Java 17 + Spring Boot 3 + JWT + PostgreSQL** y un frontend mínimo sin frameworks.

---

## 🎯 Flujo Implementado

1. AGENTE registra una venta (estado PENDIENTE).
2. BACKOFFICE aprueba o rechaza la venta.
3. SUPERVISOR consulta ventas del equipo y visualiza reportes.

---

## 🏗 Stack Tecnológico

Backend:
- Java 17
- Spring Boot 3
- Spring Security + JWT (stateless)
- Spring Data JPA
- PostgreSQL

Frontend:
- HTML + CSS + JavaScript (sin frameworks)

Arquitectura en capas:

Controller → Service → Repository → PostgreSQL

---

## 👥 Roles

| Rol          | Permisos |
|--------------|----------|
| AGENTE       | Crear ventas y ver solo sus ventas |
| BACKOFFICE   | Aprobar o rechazar ventas |
| SUPERVISOR   | Ver ventas del equipo y reportes |
| ADMIN        | Acceso total |

---

## 🔐 Autenticación

Login:

POST /api/v1/auth/login

Header requerido para endpoints protegidos:

Authorization: Bearer <token>

---

## 📦 Endpoints Principales

Agente:
- POST /ventas
- GET /ventas/mis-ventas

Backoffice:
- GET /ventas/pendientes
- POST /ventas/{id}/aprobar
- POST /ventas/{id}/rechazar

Supervisor:
- GET /ventas/equipo
- GET /reportes/resumen

El resumen incluye:
- Conteos por estado
- Monto total aprobadas
- Serie ventas por día (YYYY-MM-DD → cantidad/monto)

---

## 🗄 Base de Datos

Base de datos: telco

Ejecutar:

createdb telco
psql -d telco -f schema.sql
psql -d telco -f data.sql

---

## 👤 Usuarios Seed

| Usuario      | Password     | Rol |
|--------------|-------------|-----|
| admin        | Admin*123   | ADMIN |
| agente1      | Agente*123  | AGENTE |
| back1        | Back*123    | BACKOFFICE |
| supervisor1  | Sup*123     | SUPERVISOR |

---

## ▶ Ejecutar Backend

Configurar application.properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/telco
spring.datasource.username=postgres
spring.datasource.password=123456
spring.jpa.hibernate.ddl-auto=none
spring.sql.init.mode=always

Ejecutar:

mvn spring-boot:run

API disponible en:
Base URL: http://localhost:8080/api/v1

---

## ▶ Ejecutar Frontend

Desde la carpeta frontend:

Abrir `frontend/index.html` usando Live Preview (Microsoft).

Ejemplo:
http://127.0.0.1:3000

---

## ✅ Estado

✔ Flujo completo funcional  
✔ Seguridad por roles  
✔ Reportes implementados  
✔ Validaciones mínimas  
✔ Base de datos con datos seed  

Desarrollado como parte de una prueba técnica backend.
