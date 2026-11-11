package co.edu.unicartagena.musicapp.application;

import co.edu.unicartagena.musicapp.application.port.in.AutenticacionUseCase;
import co.edu.unicartagena.musicapp.application.port.out.NotificacionPort;
import co.edu.unicartagena.musicapp.application.port.out.PasswordHasherPort;
import co.edu.unicartagena.musicapp.application.port.out.TokenPort;
import co.edu.unicartagena.musicapp.application.port.out.UsuarioRepositoryPort;
import co.edu.unicartagena.musicapp.application.service.AutenticacionService;
import co.edu.unicartagena.musicapp.domain.exception.ConflictoException;
import co.edu.unicartagena.musicapp.domain.exception.CredencialesInvalidasException;
import co.edu.unicartagena.musicapp.domain.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AutenticacionServiceTest {

    private static final Instant AHORA = Instant.parse("2026-11-11T12:00:00Z");

    private UsuarioRepositoryPort repositorio;
    private PasswordHasherPort hasher;
    private TokenPort tokens;
    private NotificacionPort notificaciones;
    private AutenticacionService servicio;

    @BeforeEach
    void setUp() {
        repositorio = mock(UsuarioRepositoryPort.class);
        hasher = mock(PasswordHasherPort.class);
        tokens = mock(TokenPort.class);
        notificaciones = mock(NotificacionPort.class);
        when(hasher.hash(anyString())).thenAnswer(i -> "hash:" + i.getArgument(0));
        when(hasher.coincide(anyString(), anyString()))
                .thenAnswer(i -> ("hash:" + i.getArgument(0)).equals(i.getArgument(1)));
        when(repositorio.guardar(any())).thenAnswer(i -> i.getArgument(0));
        when(tokens.generar(any())).thenReturn("token-123");
        servicio = new AutenticacionService(repositorio, hasher, tokens, notificaciones,
                Clock.fixed(AHORA, ZoneOffset.UTC), 15);
    }

    private Usuario usuarioGuardado() {
        return new Usuario(1L, "oscar", "Oscar Mercado", "oscar@ejemplo.com", "hash:secreta1", null, null);
    }

    @Test
    void registraUnUsuarioConLaContrasenaCifrada() {
        var sesion = servicio.registrar(
                new AutenticacionUseCase.Registro("oscar", "Oscar Mercado", "Oscar@Ejemplo.com", "secreta1"));
        assertThat(sesion.token()).isEqualTo("token-123");
        assertThat(sesion.usuario().getPasswordHash()).isEqualTo("hash:secreta1");
        assertThat(sesion.usuario().getEmail()).isEqualTo("oscar@ejemplo.com");
    }

    @Test
    void noPermiteCorreoRepetido() {
        when(repositorio.existeEmail("oscar@ejemplo.com")).thenReturn(true);
        assertThatThrownBy(() -> servicio.registrar(
                new AutenticacionUseCase.Registro("oscar", "Oscar", "oscar@ejemplo.com", "secreta1")))
                .isInstanceOf(ConflictoException.class);
    }

    @Test
    void iniciaSesionConCorreoOUsername() {
        when(repositorio.buscarPorEmail("oscar@ejemplo.com")).thenReturn(Optional.of(usuarioGuardado()));
        when(repositorio.buscarPorUsername("oscar")).thenReturn(Optional.of(usuarioGuardado()));
        assertThat(servicio.iniciarSesion("OSCAR@ejemplo.com", "secreta1").token()).isEqualTo("token-123");
        assertThat(servicio.iniciarSesion("oscar", "secreta1").token()).isEqualTo("token-123");
    }

    @Test
    void rechazaContrasenaIncorrecta() {
        when(repositorio.buscarPorEmail("oscar@ejemplo.com")).thenReturn(Optional.of(usuarioGuardado()));
        assertThatThrownBy(() -> servicio.iniciarSesion("oscar@ejemplo.com", "otra"))
                .isInstanceOf(CredencialesInvalidasException.class);
    }

    @Test
    void recuperacionEnviaUnCodigoDeSeisDigitosYLoGuardaCifrado() {
        Usuario usuario = usuarioGuardado();
        when(repositorio.buscarPorEmail("oscar@ejemplo.com")).thenReturn(Optional.of(usuario));

        servicio.solicitarRecuperacion("oscar@ejemplo.com");

        ArgumentCaptor<String> codigo = ArgumentCaptor.forClass(String.class);
        verify(notificaciones).enviarCodigoRecuperacion(eq("oscar@ejemplo.com"), eq("Oscar Mercado"),
                codigo.capture(), eq(15));
        assertThat(codigo.getValue()).matches("\\d{6}");
        assertThat(usuario.getCodigoRecuperacionHash()).isEqualTo("hash:" + codigo.getValue());
        assertThat(usuario.getCodigoRecuperacionExpira()).isEqualTo(AHORA.plusSeconds(15 * 60));
    }

    @Test
    void recuperacionNoRevelaSiElCorreoNoExiste() {
        when(repositorio.buscarPorEmail("nadie@ejemplo.com")).thenReturn(Optional.empty());
        servicio.solicitarRecuperacion("nadie@ejemplo.com");
        verify(notificaciones, never()).enviarCodigoRecuperacion(anyString(), anyString(), anyString(), anyInt());
    }

    @Test
    void restableceLaContrasenaConUnCodigoVigente() {
        Usuario usuario = usuarioGuardado();
        usuario.asignarCodigoRecuperacion("hash:123456", AHORA.plusSeconds(60));
        when(repositorio.buscarPorEmail("oscar@ejemplo.com")).thenReturn(Optional.of(usuario));

        servicio.restablecerPassword("oscar@ejemplo.com", "123456", "nuevaClave");

        assertThat(usuario.getPasswordHash()).isEqualTo("hash:nuevaClave");
        assertThat(usuario.getCodigoRecuperacionHash()).isNull();
    }

    @Test
    void rechazaUnCodigoVencido() {
        Usuario usuario = usuarioGuardado();
        usuario.asignarCodigoRecuperacion("hash:123456", AHORA.minusSeconds(1));
        when(repositorio.buscarPorEmail("oscar@ejemplo.com")).thenReturn(Optional.of(usuario));
        assertThatThrownBy(() -> servicio.restablecerPassword("oscar@ejemplo.com", "123456", "nuevaClave"))
                .isInstanceOf(CredencialesInvalidasException.class);
    }
}
