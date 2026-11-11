package co.edu.unicartagena.musicapp.application.port.out;

/** Puerto de salida: envío de correos al usuario. */
public interface NotificacionPort {
    void enviarCodigoRecuperacion(String email, String nombre, String codigo, int minutosValidez);
}
