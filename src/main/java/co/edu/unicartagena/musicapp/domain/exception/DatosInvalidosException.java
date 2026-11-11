package co.edu.unicartagena.musicapp.domain.exception;

/** Los datos recibidos no cumplen las reglas del dominio. */
public class DatosInvalidosException extends DomainException {
    public DatosInvalidosException(String mensaje) {
        super(mensaje);
    }
}
