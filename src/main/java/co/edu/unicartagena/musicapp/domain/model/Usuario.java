package co.edu.unicartagena.musicapp.domain.model;

import co.edu.unicartagena.musicapp.domain.exception.DatosInvalidosException;

import java.time.Instant;
import java.util.regex.Pattern;

/**
 * Tabla Usuario (username, password, nombre, email).
 * La contraseña nunca se guarda en texto plano: solo su hash.
 */
public class Usuario {

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern USERNAME = Pattern.compile("^[A-Za-z0-9._-]{3,50}$");
    public static final int MIN_PASSWORD = 6;

    private Long id;
    private String username;
    private String nombre;
    private String email;
    private String passwordHash;
    private String codigoRecuperacionHash;
    private Instant codigoRecuperacionExpira;

    public Usuario(Long id, String username, String nombre, String email, String passwordHash,
                   String codigoRecuperacionHash, Instant codigoRecuperacionExpira) {
        this.id = id;
        this.username = username;
        this.nombre = nombre;
        this.email = email;
        this.passwordHash = passwordHash;
        this.codigoRecuperacionHash = codigoRecuperacionHash;
        this.codigoRecuperacionExpira = codigoRecuperacionExpira;
    }

    /** Crea un usuario nuevo validando sus datos. */
    public static Usuario nuevo(String username, String nombre, String email, String passwordHash) {
        Usuario u = new Usuario(null, null, null, null, passwordHash, null, null);
        u.actualizarPerfil(username, nombre);
        u.email = normalizarEmail(email);
        return u;
    }

    public void actualizarPerfil(String username, String nombre) {
        if (username == null || !USERNAME.matcher(username.trim()).matches()) {
            throw new DatosInvalidosException(
                    "El username debe tener entre 3 y 50 caracteres (letras, números, punto, guion o guion bajo)");
        }
        if (nombre == null || nombre.isBlank() || nombre.trim().length() > 120) {
            throw new DatosInvalidosException("El nombre es obligatorio (máximo 120 caracteres)");
        }
        this.username = username.trim();
        this.nombre = nombre.trim();
    }

    public static String normalizarEmail(String email) {
        if (email == null || !EMAIL.matcher(email.trim()).matches()) {
            throw new DatosInvalidosException("El correo no es válido");
        }
        return email.trim().toLowerCase();
    }

    public static void validarPassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD) {
            throw new DatosInvalidosException("La contraseña debe tener mínimo " + MIN_PASSWORD + " caracteres");
        }
    }

    public void cambiarPasswordHash(String nuevoHash) {
        this.passwordHash = nuevoHash;
        limpiarCodigoRecuperacion();
    }

    public void asignarCodigoRecuperacion(String codigoHash, Instant expira) {
        this.codigoRecuperacionHash = codigoHash;
        this.codigoRecuperacionExpira = expira;
    }

    public boolean codigoRecuperacionVigente(Instant ahora) {
        return codigoRecuperacionHash != null && codigoRecuperacionExpira != null
                && ahora.isBefore(codigoRecuperacionExpira);
    }

    public void limpiarCodigoRecuperacion() {
        this.codigoRecuperacionHash = null;
        this.codigoRecuperacionExpira = null;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getCodigoRecuperacionHash() { return codigoRecuperacionHash; }
    public Instant getCodigoRecuperacionExpira() { return codigoRecuperacionExpira; }
}
