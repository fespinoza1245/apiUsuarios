# API Usuarios (CRUD)

Este proyecto tiene como objetivo exponer una API para registrar un objeto de tipo **Usuario** a través de un llamado mediante herramientas como **Postman**. Se ha implementado un sistema de autenticación **JWT** para asegurar los endpoints, requiriendo un login previo para acceder a los servicios.

## 🚀 Tecnologías Utilizadas

- **Spring Boot** para la implementación del backend.
- **Java 11** como lenguaje de programación.
- **JPA** para la gestión de la base de datos.
- **Lombok** para reducir la verbosidad del código.
- **Maven** como herramienta de gestión de dependencias.
- **JWT** para la autenticación de usuarios.
- **Swagger** y **OpenAPI** para la documentación interactiva de la API.
- **JUnit 5** para las pruebas unitarias.
- **H2** como base de datos en memoria (ideal para pruebas rápidas).

## 📋 Instrucciones de Uso

### 1. Clonación del Repositorio

Para comenzar, clona el repositorio en tu máquina local:

```bash
git clone https://github.com/fespinoza1245/apiUsuarios
```

### 2. Abrir el Proyecto

Una vez descargado, abre el proyecto en tu IDE preferido. **Maven** se encargará de descargar automáticamente todas las dependencias necesarias.

### 3. Ejecución del Proyecto

Este proyecto incluye una colección de **Postman** para realizar pruebas, pero también puedes acceder a la documentación interactiva de la API utilizando **Swagger** en la siguiente URL:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### 4. Login de Usuario

Antes de probar los demás endpoints, primero debes autenticarte. Usa el siguiente comando **curl** en Postman o SoapUI para realizar el login con un usuario pre-creado:

```bash
curl --location 'http://localhost:8080/login' \
--header 'Content-Type: application/json' \
--data-raw '{"email": "usuario@ejemplo.com", "password": "miClave123"}'
```

## JWT Token

Al realizar el login, recibirás un **JWT token** que podrás usar en los siguientes endpoints. Recuerda que el token tiene una validez de 30 minutos.

Usa el **Bearer Token** en el campo de autorización para hacer solicitudes a la API:

```makefile
Authorization: Bearer <tu_token_aqui>
```

## 4. Probar Otros Endpoints

Una vez autenticado, podrás acceder a otros endpoints de la API usando el token de acceso. Utiliza los parámetros y la documentación proporcionada en **Swagger** para hacer pruebas.

## 🔩 Detalles de las Pruebas

El proyecto incluye **pruebas unitarias** implementadas con **JUnit 5** para validar los servicios y respuestas de la API. Las pruebas aseguran que los endpoints estén funcionando correctamente y los datos se manejen adecuadamente.

## 📂 Base de Datos

Para las pruebas rápidas, este proyecto usa **H2** como base de datos en memoria, lo que significa que los datos no persisten después de detener el servidor. Para un entorno de producción, se recomienda configurar **MySQL** o **PostgreSQL** para una mayor persistencia.

## ✒️ Autor

**Francisco Espinoza**