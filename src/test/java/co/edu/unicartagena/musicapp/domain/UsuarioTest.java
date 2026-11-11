package co.edu.unicartagena.musicapp.domain;

import co.edu.unicartagena.musicapp.domain.exception.DatosInvalidosException;
import co.edu.unicartagena.musicapp.domain.model.Usuario;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UsuarioTest {

    @Test
    void normalizaElCorreo() {
        Usuario u = Usuario.nuevo("oscar", "Oscar Mercado", "  OMercado@Ejemplo.COM ", "hash");
        assertThat(u.getEmail()).isEqualTo("omercado@ejemplo.com");
    }

    @Test
    void rechazaUsernameConEspacios() {
        assertThatThrownBy(() -> Usuario.nuevo("oscar mercado", "Oscar", "a@b.co", "hash"))
                .isInstanceOf(DatosInvalidosException.class);
    }

    @Test
    void rechazaCorreoInvalido() {
        assertThatThrownBy(() -> Usuario.nuevo("oscar", "Oscar", "no-es-correo", "hash"))
                .isInstanceOf(DatosInvalidosException.class);
    }

    @Test
    void elCodigoDeRecuperacionVence() {
        Usuario u = Usuario.nuevo("oscar", "Oscar", "a@b.co", "hash");
        Instant ahora = Instant.parse("2026-01-01T10:00:00Z");
        u.asignarCodigoRecuperacion("hashCodigo", ahora.plusSeconds(600));
        assertThat(u.codigoRecuperacionVigente(ahora)).isTrue();
        assertThat(u.codigoRecuperacionVigente(ahora.plusSeconds(601))).isFalse();
        u.cambiarPasswordHash("nuevo");
        assertThat(u.codigoRecuperacionVigente(ahora)).isFalse();
    }
}
