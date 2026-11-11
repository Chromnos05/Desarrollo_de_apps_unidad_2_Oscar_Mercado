package co.edu.unicartagena.musicapp.domain.exception;

/** El recurso solicitado no existe. */
public class RecursoNoEncontradoException extends DomainException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
