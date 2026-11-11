package co.edu.unicartagena.musicapp.application.service;

import co.edu.unicartagena.musicapp.application.port.in.AutenticacionUseCase;
import co.edu.unicartagena.musicapp.application.port.out.NotificacionPort;
import co.edu.unicartagena.musicapp.application.port.out.PasswordHasherPort;
import co.edu.unicartagena.musicapp.application.port.out.TokenPort;
import co.edu.unicartagena.musicapp.application.port.out.UsuarioRepositoryPort;
import co.edu.unicartagena.musicapp.domain.exception.ConflictoException;
import co.edu.unicartagena.musicapp.domain.exception.CredencialesInvalidasException;
import co.edu.unicartagena.musicapp.domain.model.Usuario;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

/** Implementación de registro, login y recordatorio de clave por email. */
public class AutenticacionService implements AutenticacionUseCase {

    private static final String MENSAJE_LOGIN = "Correo/usuario o contraseña incorrectos";
    private static final String MENSAJE_CODIGO = "El código no es válido o ya venció";

    private final UsuarioRepositoryPort repositorio;
    private final PasswordHasherPort hasher;
    private final TokenPort tokens;
    private final NotificacionPort notificaciones;
    private final Clock reloj;
    private final int minutosValidezCodigo;
    private final SecureRandom aleatorio = new SecureRandom();

    public AutenticacionService(UsuarioRepositoryPort repositorio, PasswordHasherPort hasher, TokenPort tokens,
                                NotificacionPort notificaciones, Clock reloj, int minutosValidezCodigo) {
        this.repositorio = repositorio;
        this.hasher = hasher;
        this.tokens = tokens;
        this.notificaciones = notificaciones;
        this.reloj = reloj;
        this.minutosValidezCodigo = minutosValidezCodigo;
    }

    @Override
    public Sesion registrar(Registro datos) {
        Usuario.validarPassword(datos.password());
        Usuario nuevo = Usuario.nuevo(datos.username(), datos.nombre(), datos.email(), null);
        if (repositorio.existeEmail(nuevo.getEmail())) {
            throw new ConflictoException("Ese correo ya está registrado");
        }
        if (repositorio.existeUsernameEnOtroUsuario(nuevo.getUsername(), null)) {
            throw new ConflictoException("El username '" + nuevo.getUsername() + "' ya está en uso");
        }
        nuevo.cambiarPasswordHash(hasher.hash(datos.password()));
        Usuario guardado = repositorio.guardar(nuevo);
        return new Sesion(tokens.generar(guardado), guardado);
    }

    @Override
    public Sesion iniciarSesion(String usuarioOEmail, String password) {
        if (usuarioOEmail == null || usuarioOEmail.isBlank() || password == null) {
            throw new CredencialesInvalidasException(MENSAJE_LOGIN);
        }
        String valor = usuarioOEmail.trim();
        Optional<Usuario> encontrado = valor.contains("@")
                ? repositorio.buscarPorEmail(valor.toLowerCase())
                : repositorio.buscarPorUsername(valor);
        Usuario usuario = encontrado
                .filter(u -> hasher.coincide(password, u.getPasswordHash()))
                .orElseThrow(() -> new CredencialesInvalidasException(MENSAJE_LOGIN));
        return new Sesion(tokens.generar(usuario), usuario);
    }

    @Override
    public void solicitarRecuperacion(String email) {
        String correo = Usuario.normalizarEmail(email);
        // Si el correo no existe no se informa, para no revelar qué cuentas están registradas.
        repositorio.buscarPorEmail(correo).ifPresent(usuario -> {
            String codigo = String.format("%06d", aleatorio.nextInt(1_000_000));
            Instant expira = Instant.now(reloj).plus(Duration.ofMinutes(minutosValidezCodigo));
            usuario.asignarCodigoRecuperacion(hasher.hash(codigo), expira);
            repositorio.guardar(usuario);
            notificaciones.enviarCodigoRecuperacion(usuario.getEmail(), usuario.getNombre(), codigo,
                    minutosValidezCodigo);
        });
    }

    @Override
    public void restablecerPassword(String email, String codigo, String nuevaPassword) {
        Usuario.validarPassword(nuevaPassword);
        String correo = Usuario.normalizarEmail(email);
        Usuario usuario = repositorio.buscarPorEmail(correo)
                .filter(u -> u.codigoRecuperacionVigente(Instant.now(reloj)))
                .filter(u -> codigo != null && hasher.coincide(codigo.trim(), u.getCodigoRecuperacionHash()))
                .orElseThrow(() -> new CredencialesInvalidasException(MENSAJE_CODIGO));
        usuario.cambiarPasswordHash(hasher.hash(nuevaPassword));
        repositorio.guardar(usuario);
    }

    @Override
    public Long autenticarToken(String token) {
        Long id = tokens.validar(token)
                .orElseThrow(() -> new CredencialesInvalidasException("Sesión inválida o vencida"));
        if (repositorio.buscarPorId(id).isEmpty()) {
            throw new CredencialesInvalidasException("Sesión inválida o vencida");
        }
        return id;
    }
}
