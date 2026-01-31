# Backend SGI

## Sistema de Gestión de Inventario (SGI) – Backend

### Descripción

El backend de **SGI** es una API REST desarrollada con Spring Boot que gestiona productos, inventario, órdenes, clientes y proveedores. Centraliza la lógica de negocio y garantiza la consistencia del stock mediante un modelo basado en movimientos.

---

### Arquitectura

* Arquitectura en capas (Controller, Service, Repository).
* Separación entre entidades y DTOs.
* Lógica de negocio desacoplada de la capa de presentación.

---

### Módulos Principales

#### Inventario

* Cálculo dinámico de stock a partir de movimientos.
* Validación automática antes de salidas.

#### Productos

* CRUD completo.
* Asociación con movimientos y órdenes.

#### Órdenes

* Tipos: compra y venta.
* Estados controlados por reglas de negocio.

#### Movimientos

* Registro histórico e inmutable.
* Base para auditoría y reportes.

#### Clientes y Proveedores

* Gestión básica de terceros.
* Relación directa con órdenes.

---

### Seguridad

* Integración con Spring Security.
* Roadmap: JWT y control de roles.

---

### Tecnologías

* Java 17+
* Spring Boot
* Spring Data JPA
* Hibernate
* MySQL
* Maven

---

### Estado del Proyecto

* Core funcional implementado.
* Lógica de negocio estable.
* Pendiente: documentación OpenAPI.

---

# Justificación Técnica del Proyecto – Backend SGI

## Contexto del Problema

En entornos comerciales pequeños y medianos es común encontrar una gestión de inventario basada en registros manuales, hojas de cálculo o sistemas poco integrados. Esto genera inconsistencias de stock, pérdida de trazabilidad, errores en órdenes y dificultad para escalar el negocio.

El proyecto **Sistema de Gestión de Inventario (SGI)** surge como una solución backend orientada a centralizar la lógica de negocio, garantizar la integridad de los datos y ofrecer una API robusta sobre la cual puedan construirse diferentes interfaces (web, móvil u otros sistemas).

---

## Objetivo Técnico

Diseñar e implementar un backend RESTful que:

* Centralice la lógica de inventario y órdenes.
* Garantice consistencia del stock mediante reglas de negocio claras.
* Sea escalable, mantenible y fácil de extender.
* Represente buenas prácticas reales de desarrollo backend.

---

## Enfoque Arquitectónico

El backend está construido bajo una **arquitectura en capas**, separando responsabilidades de forma explícita:

* **Controller:** Manejo de peticiones HTTP, validación básica y orquestación.
* **Service:** Núcleo de la lógica de negocio.
* **Repository:** Acceso a datos mediante Spring Data JPA.
* **Entity / DTO:** Separación entre persistencia y transporte de datos.

Este enfoque evita el acoplamiento innecesario y facilita pruebas, mantenimiento y evolución del sistema.

---

## Decisiones Técnicas Clave

### 1. Stock basado en movimientos

El stock no se almacena como un valor fijo, sino que se **deriva de los movimientos históricos**:

* Entradas
* Salidas
* Ajustes positivos
* Ajustes negativos

Esto permite:

* Trazabilidad completa del inventario.
* Auditoría de cambios.
* Corrección de inconsistencias sin perder historial.

### 2. Órdenes como eje del flujo

Las órdenes controlan los movimientos de inventario y no al contrario. Esto asegura:

* Validación de stock antes de salidas.
* Control de estados (`PENDIENTE`, `EN_PROCESO`, `COMPLETADA`, `CANCELADA`).
* Cancelaciones controladas y reversión lógica cuando aplica.

### 3. Manejo explícito de errores de negocio

El sistema utiliza excepciones personalizadas para representar reglas del dominio, evitando depender solo de errores técnicos. Esto facilita:

* Respuestas HTTP coherentes.
* Mensajes claros para el frontend.
* Mejor mantenibilidad.

---

## Escalabilidad y Evolución

El proyecto está preparado para crecer hacia:

* Multi-almacén.
* Reportes analíticos.
* Integración con frontend moderno (React).

El diseño prioriza extensibilidad sin reescrituras profundas.

---

## Valor como Proyecto de Portafolio

Este backend demuestra:

* Comprensión real de lógica de negocio.
* Uso correcto de Spring Boot y JPA.
* Modelado de dominio.
* Pensamiento orientado a sistemas reales.

---

### 📄 Licencia

MIT License


