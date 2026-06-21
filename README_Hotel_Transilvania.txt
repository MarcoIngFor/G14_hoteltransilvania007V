
HOTEL TRANSILVANIA – SISTEMA DE GESTIÓN HOTELERA BASADO EN MICROSERVICIOS

DESCRIPCIÓN DEL PROYECTO

Hotel Transilvania es una aplicación desarrollada utilizando arquitectura de microservicios, cuyo propósito es administrar 
los distintos procesos asociados a un hotel, tales como la gestión de usuarios, clientes, habitaciones, reservas, consumos, 
pagos, mantenimientos y reportes.

La aplicación fue desarrollada utilizando Spring Boot y diversas herramientas del ecosistema Spring, con el objetivo de aplicar 
los conocimientos adquiridos durante el segundo año de la carrera.

OBJETIVO

Implementar un sistema distribuido que permita administrar los distintos servicios del hotel mediante microservicios independientes 
capaces de comunicarse entre sí a través de API REST.

TECNOLOGÍAS UTILIZADAS

Backend:
- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security
- JWT
- OpenFeign
- API Gateway
- Eureka Server
- Swagger OpenAPI

Base de Datos:
- MySQL

Herramientas:
- Maven
- VS Code
- Postman
- Docker
- Docker Compose

Pruebas:
- JUnit 5
- Mockito

MICROSERVICIOS IMPLEMENTADOS

- usuarios
- clientes
- habitaciones
- reservas
- consumo
- mantenimiento
- pagos
- notificaciones
- servicioextra
- reportes
- api-gateway
- eurekaserver

COMUNICACIÓN ENTRE MICROSERVICIOS

La comunicación entre microservicios se realiza mediante OpenFeign, permitiendo el consumo de servicios REST internos.

SEGURIDAD

Se implementó autenticación mediante JWT, incorporando generación de token y protección de endpoints.

DOCUMENTACIÓN

Cada microservicio posee documentación mediante Swagger OpenAPI.

PRUEBAS UNITARIAS

Se desarrollaron pruebas unitarias utilizando JUnit 5 y Mockito para:

- Usuarios
- Clientes
- Habitaciones
- Reservas
- Consumo
- Mantenimiento
- Pagos
- Reportes
- Servicio Extra
- Tipo Servicio
- Notificaciones

DOCKER

Cada microservicio cuenta con su respectivo Dockerfile y el sistema puede ejecutarse mediante Docker Compose.

CONCLUSIONES

El desarrollo de este proyecto permitió aplicar los conocimientos adquiridos durante la asignatura de Desarrollo Full Stack, 
reforzando conceptos relacionados con arquitectura de microservicios, seguridad, persistencia, pruebas unitarias y despliegue mediante contenedores Docker.

Asimismo, permitió comprender cómo se estructura una aplicación distribuida similar a las utilizadas actualmente en entornos reales de desarrollo.
