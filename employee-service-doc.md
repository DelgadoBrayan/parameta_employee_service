# Servicio de Empleados API

API REST para el registro de empleados construida con Spring Boot 3.5.13, MySQL, Flyway y OpenAPI 3.

---

## Stack Tecnológico

- **Java 21**
- **Spring Boot 3.5.13**
- **Spring Data JPA + Hibernate**
- **MySQL 8**
- **Flyway** — migraciones de base de datos
- **SpringDoc OpenAPI 3 (v2.8.15)** — Swagger UI
- **JUnit 5 + Mockito** — pruebas unitarias

---

## Arquitectura

Arquitectura Hexagonal (Puertos y Adaptadores) con principios de Clean Architecture.

```
src/main/java/com/company/employee/
├── domain/
│   ├── model/                  # Objetos puros del dominio
│   ├── exception/              # Excepciones del dominio
│   ├── port/
│   │   ├── in/                 # Interfaces de casos de uso
│   │   └── out/                # Interfaces de puertos de repositorio
│   └── usecase/                # Implementación de la lógica de negocio
├── application/
│   ├── dto/                    # DTOs de entrada y salida
│   ├── mapper/                 # Mapeo DTO ↔ Dominio
│   └── service/                # Interfaces e implementaciones de servicio
└── infrastructure/
    ├── adapter/                # Implementación del puerto de repositorio
    ├── config/                 # Beans de Spring + configuración OpenAPI
    ├── controller/             # Controladores REST
    ├── entity/                 # Entidades JPA
    ├── repository/             # Repositorios Spring Data JPA
    ├── exception/              # Excepciones técnicas
    └── exceptionhandler/       # Manejador global de errores (@RestControllerAdvice)
```

---

## Migración de Base de Datos

Gestionada por Flyway. Los scripts se encuentran en:

```
src/main/resources/db/migration/
└── V1__create_employees_table.sql
```
---



## Swagger UI

| URL | Descripción |
|---|---|
| http://localhost:8080/swagger-ui.html | Interfaz visual interactiva |
| http://localhost:8080/v3/api-docs | Especificación OpenAPI en formato JSON |
| http://localhost:8080/v3/api-docs.yaml | Especificación OpenAPI en formato YAML |

## Levantar la Aplicación

### Pasos

```bash
# 1. Clonar el repositorio
git clone <https://github.com/DelgadoBrayan/parameta_employee_service>
cd employee-service

# 2. Configurar las credenciales de MySQL en application.properties

# 3. Levantar la aplicación (Flyway crea las tablas automáticamente)
mvn spring-boot:run
```

> La base de datos `employee_db` se crea automáticamente si no existe gracias al parámetro `createDatabaseIfNotExist=true` en la URL de conexión.