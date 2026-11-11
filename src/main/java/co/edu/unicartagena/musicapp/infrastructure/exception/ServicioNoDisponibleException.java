package co.edu.unicartagena.musicapp.infrastructure.exception;

/** Un servicio externo (por ejemplo el correo) no respondió. */
public class ServicioNoDisponibleException extends RuntimeException {
    public ServicioNoDisponibleException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
