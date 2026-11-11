package co.edu.unicartagena.musicapp.domain;

import co.edu.unicartagena.musicapp.domain.exception.DatosInvalidosException;
import co.edu.unicartagena.musicapp.domain.model.Cancion;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CancionTest {

    @Test
    void creaUnaCancionValidaYLimpiaEspacios() {
        Cancion c = new Cancion(null, "  Bohemian Rhapsody ", "Rock", 354, "A Night at the Opera", 11,
                "Queen", "Freddie Mercury", "Freddie Mercury", LocalDate.of(1975, 10, 31));
        assertThat(c.getNombre()).isEqualTo("Bohemian Rhapsody");
        assertThat(c.conId(7L).getId()).isEqualTo(7L);
    }

    @Test
    void rechazaDuracionInvalida() {
        assertThatThrownBy(() -> new Cancion(null, "X", "Rock", 0, null, null, null, null, null, null))
                .isInstanceOf(DatosInvalidosException.class)
                .hasMessageContaining("duración");
    }

    @Test
    void rechazaNombreVacio() {
        assertThatThrownBy(() -> new Cancion(null, " ", "Rock", 100, null, null, null, null, null, null))
                .isInstanceOf(DatosInvalidosException.class);
    }

    @Test
    void rechazaPosicionNegativa() {
        assertThatThrownBy(() -> new Cancion(null, "X", "Pop", 100, null, -1, null, null, null, null))
                .isInstanceOf(DatosInvalidosException.class);
    }
}
