-- =============================================================
-- MusicApp - Ejercicio 20 (Canciones)
-- Esquema de base de datos PostgreSQL (Supabase)
-- =============================================================

-- Tabla Usuario (username, password, nombre, email)
-- La contraseña se guarda como hash BCrypt, nunca en texto plano.
CREATE TABLE IF NOT EXISTS usuarios (
    id                           BIGSERIAL PRIMARY KEY,
    username                     VARCHAR(50)  NOT NULL UNIQUE,
    password_hash                VARCHAR(100) NOT NULL,
    nombre                       VARCHAR(120) NOT NULL,
    email                        VARCHAR(150) NOT NULL UNIQUE,
    codigo_recuperacion_hash     VARCHAR(100),
    codigo_recuperacion_expira   TIMESTAMPTZ,
    creado_en                    TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_usuarios_nombre ON usuarios (lower(nombre) text_pattern_ops);

-- Tabla Canciones (id, nombre, ritmo, duración, album, posicionEnAlbum, banda,
--                  interprete, autor, fechaLanzamiento)
CREATE TABLE IF NOT EXISTS canciones (
    id                  BIGSERIAL PRIMARY KEY,
    nombre              VARCHAR(150) NOT NULL,
    ritmo               VARCHAR(50)  NOT NULL,
    duracion            INTEGER      NOT NULL CHECK (duracion > 0),       -- en segundos
    album               VARCHAR(150),
    posicion_en_album   INTEGER      CHECK (posicion_en_album > 0),
    banda               VARCHAR(150),
    interprete          VARCHAR(150),
    autor               VARCHAR(150),
    fecha_lanzamiento   DATE,
    creado_en           TIMESTAMPTZ  NOT NULL DEFAULT now()
);

-- Índices para los reportes parametrizados
CREATE INDEX IF NOT EXISTS idx_canciones_ritmo ON canciones (lower(ritmo));
CREATE INDEX IF NOT EXISTS idx_canciones_fecha ON canciones (fecha_lanzamiento);
CREATE INDEX IF NOT EXISTS idx_canciones_duracion ON canciones (duracion);

-- El acceso es solo a través de la API (Spring Boot). Se activa RLS sin políticas
-- para que nadie pueda leer/escribir las tablas directamente con la API pública de Supabase.
ALTER TABLE usuarios  ENABLE ROW LEVEL SECURITY;
ALTER TABLE canciones ENABLE ROW LEVEL SECURITY;
