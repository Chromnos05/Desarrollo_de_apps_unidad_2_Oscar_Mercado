package co.edu.unicartagena.musicapp.application.port.out;

import co.edu.unicartagena.musicapp.domain.model.Usuario;

import java.util.Optional;

/** Puerto de salida: emisión y validación de tokens de sesión. */
public interface TokenPort {
    String generar(Usuario usuario);
    Optional<Long> validar(String token);
}
