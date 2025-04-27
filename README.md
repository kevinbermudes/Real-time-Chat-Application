# 🛡️ Auth_base - Proyecto Base con Spring Boot

Proyecto base en Spring Boot que incluye autenticación segura con JWT, subida de archivos, comunicación en tiempo real mediante WebSockets y documentación interactiva con Swagger. Ideal para comenzar desarrollos de APIs modernas y organizadas sin complicaciones.

---

## 🚀 Características

- 🔐 **Autenticación JWT**: Registro, inicio de sesión y validación de tokens con control de roles.
- 💬 **WebSockets**: Comunicación en tiempo real lista para usar.
- 🗂️ **Almacenamiento de archivos**: Subida y eliminación automática al iniciar la app.
- 📄 **Swagger/OpenAPI**: Documentación generada automáticamente y disponible desde el navegador.
- 🌐 **CORS configurado**: Soporte para peticiones desde distintos orígenes.
- 🔄 **Paginación personalizada** con `PageResponse<T>`.

---

## ⚙️ Requisitos

- Java 17+
- Maven 3.8+
- Docker (opcional para producción)
- Spring Boot 3.x

---

## 🧠 Cosas importantes a tener en cuenta 🧠

### 1. 👤 Usuario insertado manualmente
Durante el desarrollo, un usuario se **inserta manualmente** en la base de datos. Recuerda modificar esto antes de pasar a producción o automatizarlo con un `data.sql` o `CommandLineRunner`.

### 2. 🧹 Archivos se eliminan al iniciar
Cada vez que se reinicia la aplicación, **todos los archivos subidos se eliminan automáticamente**. Esto se define en la propiedad:

```properties
upload.delete=true
```

Si no deseas esta funcionalidad en producción, cambia esa propiedad a `false`.

### 3. 🔐 Certificado autofirmado
El certificado SSL que se incluye por defecto es solo para desarrollo. **Es necesario reemplazarlo por uno válido** antes de desplegar en producción. Puedes usar Let's Encrypt o tu propio certificado.

---

## 🧪 Endpoints principales

### 🔒 Autenticación
- `POST /v1/auth/signup` → Registro de usuario
- `POST /v1/auth/signin` → Inicio de sesión (devuelve token JWT)

### 📁 Almacenamiento
- `POST /storage/upload` → Subida de archivos
- `GET /storage/files` → Listado
- `GET /storage/files/{filename}` → Descargar archivo
- `DELETE /storage/files/{filename}` → Eliminar archivo

### 🔌 WebSocket
- `/ws/{entidad}` → Canal para recibir mensajes desde el servidor

---

## 🧰 Cómo ejecutar el proyecto

```bash
# Clona el proyecto
git clone https://github.com/kevinbermudes/Auth_base.git
cd Auth_base

# Compila y ejecuta
./mvnw spring-boot:run
```

> Puedes cambiar el perfil con:  
> `--spring.profiles.active=dev` o `prod`

---

## 📚 Documentación Swagger

Disponible en:
```
http://localhost:8080/swagger-ui/index.html
```

---

## 📝 Licencia

Este proyecto está bajo la licencia [CC BY-NC-SA 4.0](https://creativecommons.org/licenses/by-nc-sa/4.0/)

---

## 💡 Recomendaciones para producción

- Usar PostgreSQL o MySQL en lugar de H2.
- Configurar variables de entorno para claves JWT.
- Reemplazar certificado SSL autofirmado.
- Validar roles y permisos a nivel de método con `@PreAuthorize`.

---
