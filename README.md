# TechnicalTest API (Spring Boot + Postgres)

## Prerrequisitos
- Docker Desktop funcionando y en modo Linux containers
- Puerto `5432` y `8080` libres

## Configuración
- Crea/edita `.env` en la raíz del proyecto:
```
POSTGRES_USER=postgres
POSTGRES_PASSWORD=<tu_password_segura>
JWT_SECRET=<clave_de_32+_bytes_para_firmar_JWT>
```
- La clave JWT debe tener mínimo 32 bytes; recomienda 64+ caracteres.

## Arranque
- Levantar BD y app:
```
docker compose up -d --build
```
- Ver estado y logs:
```
docker compose ps
docker logs technicaltest-app --follow
```
- Swagger UI:
```
http://localhost:8080/swagger-ui/index.html
```
- OpenAPI JSON/YAML:
```
http://localhost:8080/v3/api-docs
http://localhost:8080/v3/api-docs.yaml
```

## Salud de BD
- Endpoint:
```
GET http://localhost:8080/db/health
```
- Respuestas: `200 OK` con `OK` o `503 Service Unavailable` con `UNAVAILABLE`.

## Autenticación (JWT)
- Login:
```
POST http://localhost:8080/auth/login
Body: {"usuario":"alopez","contrasena":"Secreta123!"}
Respuesta: {"token":"<JWT>","tokenType":"Bearer"}
```
- Usa el token en el header:
```
Authorization: Bearer <JWT>
```
- Rutas públicas: `/auth/login`, `/db/health`, `/swagger-ui/**`, `/v3/api-docs/**`
- Resto de endpoints requieren `Authorization`.

## Usuarios (CRUD)
- Crear usuario:
```
POST http://localhost:8080/users
Content-Type: application/json
{
  "nombres":"Ana",
  "apellidos":"Lopez",
  "usuario":"alopez",
  "contrasena":"Secreta123!"
}
```
- Obtener usuario:
```
GET http://localhost:8080/users/{id}
```
- Listar usuarios:
```
GET http://localhost:8080/users
```
- Actualizar usuario:
```
PUT http://localhost:8080/users/{id}
Content-Type: application/json
{
  "nombres":"Ana",
  "apellidos":"Lopez",
  "usuario":"alopez",
  "contrasena":"NuevaSecreta123!"
}
```
- Eliminar usuario:
```
DELETE http://localhost:8080/users/{id}
```
- Validación: todos los campos son requeridos y `contrasena` mínima 8 caracteres.

## Tickets (CRUD + filtros + cierre)
- Crear ticket (requiere JWT):
```
POST http://localhost:8080/tickets
Authorization: Bearer <JWT>
Content-Type: application/json
{
  "descripcion":"Error en pantalla principal",
  "usuarioId":"<UUID_usuario>"
}
```
- Obtener ticket:
```
GET http://localhost:8080/tickets/{id}
Authorization: Bearer <JWT>
```
- Listar tickets con filtros:
```
GET http://localhost:8080/tickets?status=ABIERTO
GET http://localhost:8080/tickets?usuarioId=<UUID>
GET http://localhost:8080/tickets?status=CERRADO&usuarioId=<UUID>
Authorization: Bearer <JWT>
```
- Actualizar ticket:
```
PUT http://localhost:8080/tickets/{id}
Authorization: Bearer <JWT>
Content-Type: application/json
{
  "descripcion":"Detalle reproducible",
  "usuarioId":"<UUID_usuario>",
  "status":"ABIERTO"
}
```
- Eliminar ticket:
```
DELETE http://localhost:8080/tickets/{id}
Authorization: Bearer <JWT>
```
- Cerrar ticket:
```
POST http://localhost:8080/tickets/{id}/close
Authorization: Bearer <JWT>
Respuestas: 200 OK si se cierra; 409 Conflict si ya estaba cerrado; 404 si no existe.
```

## Caché (Caffeine)
- El listado `GET /tickets?usuarioId=<UUID>` se cachea 5 min por usuario.
- Operaciones de creación/actualización/cierre/eliminación invalidan la entrada de caché del usuario.

## Base de datos
- Base: `dvpdb`
- Scripts SQL iniciales cargados al arranque:
  - `V1__create_users.sql`: tabla `users`
  - `V2__create_tickets.sql`: tabla `tickets` con `usuario_id` y `status` (`ABIERTO/CERRADO`)

## Pruebas unitarias
- Ejecutar pruebas con contenedor Maven (Windows PowerShell):
```
docker run --rm -v "${PWD}:/app" -w /app maven:3.9.9-eclipse-temurin-17 mvn test
```
- Ejecutar pruebas con contenedor Maven (CMD):
```
docker run --rm -v "%cd%:/app" -w /app maven:3.9.9-eclipse-temurin-17 mvn test
```
- Áreas cubiertas: login/JWT, codificación de contraseñas, cierre de tickets y controladores con MockMvc.

## Detener y mantenimiento
- Parar servicios conservando datos:
```
docker compose down
```
- Parar y borrar datos (volumen de Postgres):
```
docker compose down -v
```
- Reinicio rápido tras cambios de `.env`:
```
docker compose up -d --build
```

## Notas de seguridad
- No compartas `JWT_SECRET` ni credenciales.
- Usa `JWT_SECRET` con 32+ bytes.
- Contraseñas se almacenan con `BCrypt`.

## Arquitectura
- Hexagonal: puertos en dominio (`UserRepositoryPort`, `TicketRepositoryPort`), servicios de aplicación (`UserService`, `TicketService`), adaptadores de infraestructura (`Jdbc*RepositoryAdapter`), entrada HTTP (controladores).
- Configuración externa via `application.yml` y variables de entorno.
