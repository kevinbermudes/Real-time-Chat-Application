
# Chat App 🚀 (En Desarrollo)

Este proyecto es una **aplicación de chat en tiempo real** construida con:
- **Spring Boot 3** (Backend)
- **MongoDB** (Base de datos)
- **WebSocket + STOMP** (Comunicación en tiempo real)
- **Angular** (Frontend, próximamente)
- **Docker** (Contenerización)

## Características principales (planeadas 🛠️)
- Registro e inicio de sesión usando **JWT**.
- Control de acceso basado en **Roles y Permisos** (Admin, Moderador, Usuario).
- **Grupos de chat** privados y públicos.
- **Mensajería en tiempo real** usando WebSocket.
- **Notificaciones** al recibir mensajes.
- Sistema de **gestión de usuarios y grupos** (por rol).
- **Docker Compose** para levantar MongoDB y el Backend fácilmente.

## Tecnologías
- Java 17
- Spring Boot 3
- Spring Security (JWT)
- Spring WebSocket (STOMP)
- MongoDB
- Docker
- Angular (futuro)

## Estado actual
🔧 **Backend en construcción**  
🔜 Frontend en Angular comenzará pronto

## Cómo levantar MongoDB (Docker)
```bash
docker-compose up -d
```

> Se levantará una instancia de MongoDB disponible en `localhost:27017`.

## Cómo correr el Backend
1. Construir el proyecto:
   ```bash
   ./mvnw clean package
   ```
2. Crear la imagen Docker:
   ```bash
   docker build -t chat-backend .
   ```
3. Correr el contenedor:
   ```bash
   docker run -p 8080:8080 chat-backend
   ```

## Autor
**Kevin Bermúdez**

---

# 🧹 Notas importantes
- Este proyecto está en desarrollo.  
- A medida que avancemos, se actualizará este README y la documentación general.
