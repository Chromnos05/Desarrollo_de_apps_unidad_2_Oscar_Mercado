package co.edu.unicartagena.musicapp.domain.exception;

/** Excepción base de las reglas de negocio. */
public abstract class DomainException extends RuntimeException {
    protected DomainException(String mensaje) {
        super(mensaje);
    }
}
