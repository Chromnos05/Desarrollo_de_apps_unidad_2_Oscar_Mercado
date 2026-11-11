# MusicApp – Unidad 2: Kodular + API REST (Spring Boot, arquitectura hexagonal)

Aplicación móvil en **Kodular** para el **Ejercicio N° 20 – Canciones**, con persistencia en una **API REST propia** escrita en **Java 21 + Spring Boot**, organizada con **arquitectura hexagonal**, desplegada en **Vercel** y conectada a una base de datos **PostgreSQL en Supabase**.

| Dato | Valor |
|---|---|
| Estudiante | 75022410035 – Oscar Mercado |
| Email institucional | omercador@unicartagena.edu.co |
| Ejercicio | 20 – Canciones |
| Actividad | Unidad 2 – Kodular con persistencia en API REST online y sensores |
| API en producción | https://musicapp-api-ten.vercel.app/api/health |
| Video explicativo | _Pendiente_ |

---

## 1. Requerimientos y cómo se cumplen

| Requerimiento | Implementación |
|---|---|
| Base de datos gratuita en internet | PostgreSQL en **Supabase** (plan gratuito) |
| CRUD de las 2 tablas | `/api/canciones` (crear, listar, ver, actualizar, eliminar) y `/api/usuarios/me` (registro, ver, actualizar, eliminar) |
| 2 o más reportes parametrizados por tabla | Canciones: por ritmo, por rango de fechas y por duración · Usuarios: por username y por nombre que empieza por… |
| Login | `POST /api/auth/login` con correo **o** username; devuelve un token **JWT** |
| Recordatorio de clave por email | `POST /api/auth/recuperar` envía un **código de 6 dígitos** por correo (SMTP) y `POST /api/auth/restablecer` cambia la contraseña |
| Sensores en la app | **Huella digital** (entrar sin escribir la contraseña) y **acelerómetro** (agitar para actualizar la lista) |
| Despliegue gratuito con CD | **Vercel** (Container Runtime): cada `push`/`merge` a `main` se despliega automáticamente |
| Arquitectura hexagonal | Dominio y casos de uso sin dependencias de frameworks; adaptadores en `infrastructure` |

---

## 2. Arquitectura hexagonal

```
src/main/java/co/edu/unicartagena/musicapp
├── domain                      ← núcleo: reglas de negocio, sin Spring
│   ├── model                   Usuario, Cancion (con sus validaciones)
│   └── exception               DatosInvalidos, RecursoNoEncontrado, Conflicto, CredencialesInvalidas
├── application
│   ├── port
│   │   ├── in                  Casos de uso: AutenticacionUseCase, UsuarioUseCase, CancionUseCase
│   │   └── out                 Puertos: repositorios, PasswordHasherPort, TokenPort, NotificacionPort
│   └── service                 Implementación de los casos de uso (Java puro)
└── infrastructure
    ├── adapter
    │   ├── in/web              Controladores REST, DTOs, interceptor de token, manejo de errores
    │   └── out
    │       ├── persistence     JPA + Spring Data (PostgreSQL / Supabase)
    │       ├── security        BCrypt (contraseñas) y JWT (sesiones)
    │       └── mail            SMTP (código de recuperación)
    └── config                  Conexión de casos de uso con adaptadores, CORS e interceptores
```

Los servicios de `application` solo conocen **interfaces** (puertos). Spring crea los adaptadores y los inyecta en `CasosDeUsoConfig`. Así se podría cambiar Supabase por otra base de datos, o SMTP por otro proveedor, sin tocar el dominio.

---

## 3. Endpoints

Base: `https://musicapp-api-ten.vercel.app/api`

Las rutas marcadas con 🔒 requieren el encabezado `Authorization: Bearer <token>`.

### Autenticación (públicas)
| Método | Ruta | Cuerpo |
|---|---|---|
| POST | `/auth/registro` | `{"username","nombre","email","password"}` → `201 {token, usuario}` |
| POST | `/auth/login` | `{"usuario": "correo o username", "password"}` → `{token, usuario}` |
| POST | `/auth/recuperar` | `{"email"}` → envía un código de 6 dígitos (vence en 15 min) |
| POST | `/auth/restablecer` | `{"email","codigo","nuevaPassword"}` |

### Usuarios 🔒
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/usuarios` | Lista de usuarios |
| GET / PUT / DELETE | `/usuarios/me` | Ver, actualizar (`{"username","nombre"}`) o eliminar la cuenta propia |
| PUT | `/usuarios/me/password` | `{"passwordActual","nuevaPassword"}` |
| GET | `/usuarios/reportes/username?valor=ana` | **Reporte 1**: username exacto |
| GET | `/usuarios/reportes/nombre?prefijo=An` | **Reporte 2**: nombre que empieza por… |

### Canciones 🔒
| Método | Ruta | Descripción |
|---|---|---|
| GET / POST | `/canciones` | Listar / crear |
| GET / PUT / DELETE | `/canciones/{id}` | Ver / actualizar / eliminar |
| GET | `/canciones/reportes/ritmo?ritmo=Rock` | **Reporte 1**: por ritmo |
| GET | `/canciones/reportes/fechas?desde=2000-01-01&hasta=2010-12-31` | **Reporte 2**: por fecha de lanzamiento |
| GET | `/canciones/reportes/duracion?min=180&max=300` | **Reporte 3**: por duración en segundos |

Ejemplo de canción:
```json
{
  "nombre": "Bohemian Rhapsody", "ritmo": "Rock", "duracion": 354,
  "album": "A Night at the Opera", "posicionEnAlbum": 11, "banda": "Queen",
  "interprete": "Freddie Mercury", "autor": "Freddie Mercury", "fechaLanzamiento": "1975-10-31"
}
```

Los errores se devuelven así: `{"estado":400,"error":"Bad Request","mensaje":"La duración debe ser ..."}`.

---

## 4. Ejecutar la API en local

**Requisitos:** JDK 21 y Maven 3.9 o superior.

```bash
git clone https://github.com/Chromnos05/Desarrollo_de_apps_unidad_2_Oscar_Mercado.git
cd Desarrollo_de_apps_unidad_2_Oscar_Mercado
cp .env.example .env        # completa los valores (ver sección 5)
export $(grep -v '^#' .env | xargs)
mvn spring-boot:run
# → http://localhost:8080/api/health
```

Pruebas unitarias: `mvn test`. También se ejecutan en GitHub Actions en cada push.

Si `MAIL_HOST` está vacío, la API no envía correos y escribe el código de recuperación en el log (útil para desarrollo).

---

## 5. Base de datos y variables de entorno

1. Crea un proyecto en [Supabase](https://supabase.com) y ejecuta `database/schema.sql` en el **SQL Editor**.
2. En **Connect → Transaction pooler** copia el host, el puerto (6543) y el usuario.
3. Define las variables de entorno:

| Variable | Ejemplo / descripción |
|---|---|
| `PORT` | `8080` |
| `DB_URL` | `jdbc:postgresql://aws-0-<region>.pooler.supabase.com:6543/postgres?prepareThreshold=0&sslmode=require` |
| `DB_USER` | `postgres.<project-ref>` |
| `DB_PASSWORD` | contraseña de la base de datos |
| `JWT_SECRET` | texto aleatorio de 32 caracteres o más |
| `MAIL_HOST` / `MAIL_PORT` | `smtp.gmail.com` / `587` |
| `MAIL_USERNAME` / `MAIL_PASSWORD` | cuenta de Gmail y su **contraseña de aplicación** |

---

## 6. Despliegue en Vercel (CD automático)

1. En Vercel: **Add New → Project → Import** este repositorio. Vercel detecta `Dockerfile.vercel` y usa el preset **Container**.
2. En **Settings → Environment Variables** agrega las variables de la sección 5.
3. Cada `git push` o `git merge` sobre `main` genera un despliegue de producción automáticamente. Las ramas `feature/*` generan despliegues de vista previa.

Flujo de trabajo usado en este repositorio:
```bash
git checkout -b feature/nueva-funcionalidad
# ... cambios y commits ...
git checkout main
git merge --no-ff feature/nueva-funcionalidad
git push origin main          # → GitHub Actions (pruebas) + Vercel (despliegue)
```

---

## 7. App móvil (Kodular)

La carpeta `app-kodular/` contiene el proyecto `AppCancionesAPI.aia` y el APK.

1. Instala `app-kodular/AppCancionesAPI.apk` en un celular Android 6.0 o superior (permite *orígenes desconocidos*).
2. **Regístrate**, inicia sesión y usa el menú: Canciones (CRUD), Mi perfil, Reportes.
3. **Olvidé mi contraseña**: escribe tu correo → *Enviar código* → escribe el código recibido y la nueva contraseña.
4. **Sensores:**
   - **Huella digital**: al volver a abrir la app aparece *Entrar con huella digital*.
   - **Acelerómetro**: en *Canciones*, agita el celular para actualizar la lista.

Para editar la app: en [Kodular Creator](https://creator.kodular.io) usa **Import project** y elige `AppCancionesAPI.aia`. La URL de la API está en la variable global `API` de cada pantalla.

> La primera petición después de un rato sin uso puede tardar unos segundos mientras el servidor arranca (arranque en frío del contenedor).

---

## 8. Tecnologías

Java 21 · Spring Boot 3.5 (Web, Data JPA, Validation, Mail) · Spring Security Crypto (BCrypt) · JJWT · PostgreSQL (Supabase) · JUnit 5 + Mockito · GitHub Actions · Docker · Vercel · Kodular.
