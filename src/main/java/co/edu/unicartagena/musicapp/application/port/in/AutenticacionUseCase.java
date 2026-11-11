package co.edu.unicartagena.musicapp.application.port.in;

import co.edu.unicartagena.musicapp.domain.model.Usuario;

/** Casos de uso: registro, inicio de sesión y recordatorio de clave por email. */
public interface AutenticacionUseCase {

    record Registro(String username, String nombre, String email, String password) { }

    record Sesion(String token, Usuario usuario) { }

    Sesion registrar(Registro datos);

    Sesion iniciarSesion(String usuarioOEmail, String password);

    /** Envía un código de 6 dígitos al correo (si está registrado). */
    void solicitarRecuperacion(String email);

    /** Cambia la contraseña usando el código recibido por correo. */
    void restablecerPassword(String email, String codigo, String nuevaPassword);

    /** Devuelve el id del usuario dueño del token, o lanza CredencialesInvalidasException. */
    Long autenticarToken(String token);
}
