package co.edu.unicartagena.musicapp.domain.exception;

/** El recurso ya existe (por ejemplo, un correo o username repetido). */
public class ConflictoException extends DomainException {
    public ConflictoException(String mensaje) {
        super(mensaje);
    }
}
