package co.edu.unicartagena.musicapp.application.port.out;

/** Puerto de salida: cifrado de contraseñas y códigos. */
public interface PasswordHasherPort {
    String hash(String textoPlano);
    boolean coincide(String textoPlano, String hash);
}
