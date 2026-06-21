# 🏨 Hotel Transilvania

<div align="center">

Sistema de gestión hotelera desarrollado mediante arquitectura de microservicios utilizando Spring Boot.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8-blue)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED)
![JWT](https://img.shields.io/badge/Security-JWT-red)
![Swagger](https://img.shields.io/badge/API-Swagger-success)
![JUnit5](https://img.shields.io/badge/Test-JUnit5-green)

</div>

---

# 📌 Descripción

Hotel Transilvania es una aplicación desarrollada utilizando arquitectura de microservicios. Su objetivo es administrar las distintas áreas de un hotel, permitiendo gestionar usuarios, clientes, habitaciones, reservas, consumos, pagos, mantenimientos, servicios extra, notificaciones y reportes.

Este proyecto fue desarrollado como parte de la asignatura Desarrollo Full Stack, aplicando los conocimientos adquiridos durante el segundo año de la carrera.

---

# 🎯 Objetivos

- Implementar una arquitectura basada en microservicios.
- Aplicar principios de separación por capas (Controller-Service-Repository).
- Implementar seguridad mediante JWT.
- Utilizar API Gateway y Eureka Server.
- Documentar servicios mediante Swagger.
- Incorporar pruebas unitarias utilizando JUnit y Mockito.
- Desplegar la aplicación utilizando Docker.

---

# 🛠 Tecnologías utilizadas

## Backend

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security
- Spring Cloud OpenFeign
- Spring Cloud Gateway
- Eureka Server
- JWT
- Swagger OpenAPI

## Base de datos

- MySQL

## Herramientas

- Maven
- Docker
- Docker Compose
- VS Code
- Postman

## Pruebas

- JUnit 5
- Mockito

---

# 🏗 Arquitectura del sistema

```text
                API Gateway
                     │
                     ▼
             Eureka Server
                     │
──────────────────────────────────
│ usuarios                       │
│ clientes                       │
│ habitaciones                   │
│ reservas                       │
│ consumo                        │
│ mantenimiento                  │
│ pagos                          │
│ servicioextra                  │
│ notificaciones                 │
│ reportes                       │
──────────────────────────────────
```

---

# 📦 Microservicios implementados

| Microservicio | Función |
|--------------|---------|
| usuarios | Autenticación y gestión de usuarios |
| clientes | Administración de clientes |
| habitaciones | Gestión de habitaciones |
| reservas | Administración de reservas |
| consumo | Registro de consumos |
| mantenimiento | Gestión de mantenimientos |
| pagos | Registro de pagos |
| servicioextra | Servicios adicionales |
| notificaciones | Notificaciones internas |
| reportes | Generación de reportes |
| api-gateway | Punto de entrada al sistema |
| eurekaserver | Descubrimiento de servicios |

---

# 🔄 Comunicación entre microservicios

El proyecto utiliza OpenFeign para permitir la comunicación entre servicios.

### Reservas

- Reservas → Clientes
- Reservas → Habitaciones
- Reservas → Notificaciones

### Consumo

- Consumo → Clientes
- Consumo → Habitaciones
- Consumo → Reservas
- Consumo → Servicio Extra

### Pagos

- Pagos → Reservas
- Pagos → Consumo

### Mantenimiento

- Mantenimiento → Habitaciones

---

# 🔐 Seguridad

La autenticación se realiza mediante JWT.

Roles implementados:

- ADMIN
- RECEPCIONISTA

Características:

- Login seguro.
- Generación de token.
- Filtro JWT.
- Protección de endpoints.
- Control de acceso por roles.

---

# 📖 Documentación

Cada microservicio dispone de documentación Swagger.

Ejemplo:

```text
http://localhost:8081/swagger-ui/index.html
```

---

# 🧪 Pruebas Unitarias

Se implementaron pruebas utilizando:

- JUnit 5
- Mockito

Microservicios cubiertos:

- Usuarios
- Clientes
- Habitaciones
- Reservas
- Consumo
- Mantenimiento
- Pagos
- Reportes
- Notificaciones
- Servicio Extra
- Tipo Servicio

---

# 📋 Reglas de negocio principales

### Reservas

✔ No se permiten reservas traslapadas.

✔ La fecha de salida debe ser posterior a la fecha de entrada.

✔ La habitación debe encontrarse disponible.

### Mantenimiento

✔ Una habitación en mantenimiento queda automáticamente no disponible.

✔ Al finalizar el mantenimiento vuelve a estar disponible.

### Pagos

✔ Solo puede existir un pago por reserva.

✔ El monto total considera habitación y consumos.

### Usuarios

✔ Solo usuarios activos pueden iniciar sesión.

✔ Contraseñas protegidas mediante BCrypt.

---

# 🐳 Docker

Levantar proyecto:

```bash
docker compose up --build
```

Detener proyecto:

```bash
docker compose down
```

---

# ▶ Ejecución de pruebas

Dentro de cada microservicio:

```bash
./mvnw test
```

o en Windows:

```powershell
.\mvnw.cmd test
```

---

# 📚 Estructura del proyecto

```text
hoteltransilvania
│
├── api-gateway
├── eurekaserver
├── usuarios
├── clientes
├── habitaciones
├── reservas
├── consumo
├── mantenimiento
├── pagos
├── servicioextra
├── notificaciones
├── reportes
├── docker-compose.yml
└── README.md
```

---

# 👨‍💻 Autor

Proyecto desarrollado para la asignatura **Desarrollo Full Stack**.

**Duoc UC**  
Segundo Año - Analista Programador  
2026

---

# ⭐ Conclusiones

Este proyecto permitió aplicar conceptos relacionados con:

- Arquitectura de microservicios.
- Seguridad mediante JWT.
- Persistencia con JPA.
- Comunicación entre servicios mediante OpenFeign.
- Documentación con Swagger.
- Contenedores Docker.
- Pruebas unitarias con JUnit y Mockito.

Asimismo, permitió adquirir experiencia en el desarrollo de aplicaciones distribuidas similares a las utilizadas en entornos reales.
