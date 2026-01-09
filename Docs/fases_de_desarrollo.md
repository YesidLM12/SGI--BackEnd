# 📦 Sistema de Gestión de Inventario (SGI)

Este documento describe las **fases de desarrollo** del proyecto SGI, organizadas de forma incremental y profesional, permitiendo construir el sistema de manera sólida, escalable y mantenible.

---

## 🧱 FASE 0 – Preparación y Cimientos ✅

**Estado:** Completada

### Objetivo

Establecer una base técnica sólida para el desarrollo del sistema.

### Alcance

* Creación del repositorio y estructura del proyecto.
* Configuración inicial de Spring Boot.
* Conexión a la base de datos MySQL.
* Configuración de JPA / Hibernate.
* Definición inicial de entidades.
* Organización de paquetes (controllers, services, repositories, entities, dto).
* Configuración de dependencias (Lombok, Validation, etc.).

### Resultado

* El proyecto levanta correctamente.
* Conexión a base de datos estable.
* Arquitectura base lista para crecer.

---

## 🔐 FASE 1 – Usuarios, Autenticación y Seguridad (RBAC)

### Objetivo

Gestionar el acceso al sistema mediante autenticación segura y control de permisos por rol.

### Funcionalidades

* Registro y login de usuarios.
* Autenticación con JWT.
* Roles del sistema:

  * Admin
  * Almacenista
  * Vendedor
* Protección de endpoints según rol.
* Auditoría básica de acciones críticas.

### Resultado esperado

* Sistema seguro.
* Acceso controlado por roles.
* Base para toda la lógica del negocio.

---

## 📦 FASE 2 – Módulo de Inventario

### Objetivo

Controlar productos y existencias de manera confiable.

### Funcionalidades

* CRUD de productos.
* Gestión de categorías.
* Control de stock actual.
* Definición de stock mínimo.
* Registro de movimientos de inventario:

  * Entrada
  * Salida
  * Ajuste

### Regla de negocio

El stock **no se modifica directamente**, solo a través de movimientos.

### Resultado esperado

* Inventario trazable.
* Historial completo de cambios.
* Base sólida para reportes.

---

## 🧑‍🤝‍🧑 FASE 3 – Clientes y Proveedores

### Objetivo

Gestionar las entidades externas relacionadas con el inventario.

### Funcionalidades

* CRUD de clientes.
* CRUD de proveedores.
* Asociación de:

  * Entradas de inventario con proveedores.
  * Salidas de inventario con clientes.

### Resultado esperado

* Relación clara entre movimientos y terceros.
* Trazabilidad comercial.

---

## 🧾 FASE 4 – Gestión de Órdenes (Compra y Venta)

### Objetivo

Implementar flujos reales de negocio.

### Funcionalidades

* Creación de órdenes de compra y venta.
* Estados de orden:

  * Pendiente
  * En proceso
  * Completado
  * Cancelado
* Validaciones de negocio:

  * No vender sin stock disponible.
  * Impacto automático en inventario.

### Resultado esperado

* Flujo profesional de órdenes.
* Lógica de negocio consistente.

---

## 📊 FASE 5 – Reportes y Analítica

### Objetivo

Transformar datos en información útil.

### Funcionalidades

* Reportes de:

  * Productos más vendidos.
  * Movimientos por rango de fechas.
  * Alertas de stock bajo.
* Exportación de reportes en formato CSV.
* Dashboard con gráficos e indicadores clave.

### Resultado esperado

* Visibilidad del negocio.
* Valor analítico del sistema.

---

## 🚀 FASE 6 – Escalabilidad y Funcionalidades Avanzadas

### Objetivo

Elevar el sistema a un nivel más profesional.

### Funcionalidades opcionales

* Multi-almacén.
* Valoración de inventario (FIFO / PEPS).
* Integración con lectores de códigos de barras.
* Sistema de notificaciones.
* Tests unitarios y de integración.
* Dockerización.
* Documentación completa con Swagger.

### Resultado esperado

* Sistema escalable y listo para producción.
* Proyecto destacable para portafolio profesional.

---

## 📌 Nota de Desarrollo

El proyecto debe avanzarse **fase por fase**, asegurando que cada una quede funcional antes de continuar con la siguiente. El enfoque recomendado es avanzar de forma **vertical**, completando cada módulo con lógica real y usable.
