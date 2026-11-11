package co.edu.unicartagena.musicapp.infrastructure.adapter.out.security;

import co.edu.unicartagena.musicapp.application.port.out.PasswordHasherPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/** Adaptador de salida: cifra contraseñas y códigos de recuperación con BCrypt. */
@Component
public class BCryptPasswordHasherAdapter implements PasswordHasherPort {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    @Override
    public String hash(String textoPlano) {
        return encoder.encode(textoPlano);
    }

    @Override
    public boolean coincide(String textoPlano, String hash) {
        if (textoPlano == null || hash == null || hash.isBlank()) {
            return false;
        }
        return encoder.matches(textoPlano, hash);
    }
}
