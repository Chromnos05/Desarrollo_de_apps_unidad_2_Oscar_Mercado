package co.edu.unicartagena.musicapp.domain.exception;

/** Correo/contraseña incorrectos, token inválido o código de recuperación inválido. */
public class CredencialesInvalidasException extends DomainException {
    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}
