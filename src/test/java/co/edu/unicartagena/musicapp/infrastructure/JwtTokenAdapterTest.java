package co.edu.unicartagena.musicapp.infrastructure;

import co.edu.unicartagena.musicapp.domain.model.Usuario;
import co.edu.unicartagena.musicapp.infrastructure.adapter.out.security.JwtTokenAdapter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtTokenAdapterTest {

    private static final String SECRETO = "clave-de-pruebas-con-mas-de-32-caracteres!!";

    @Test
    void generaYValidaUnToken() {
        JwtTokenAdapter adapter = new JwtTokenAdapter(SECRETO, 1);
        Usuario u = new Usuario(42L, "oscar", "Oscar", "a@b.co", "h", null, null);
        String token = adapter.generar(u);
        assertThat(adapter.validar(token)).contains(42L);
    }

    @Test
    void rechazaTokensAlteradosOFirmadosConOtraClave() {
        JwtTokenAdapter adapter = new JwtTokenAdapter(SECRETO, 1);
        JwtTokenAdapter otro = new JwtTokenAdapter(SECRETO + "-distinta", 1);
        String token = otro.generar(new Usuario(1L, "x", "X", "a@b.co", "h", null, null));
        assertThat(adapter.validar(token)).isEmpty();
        assertThat(adapter.validar("no-es-un-token")).isEmpty();
    }

    @Test
    void exigeUnSecretoLargo() {
        assertThatThrownBy(() -> new JwtTokenAdapter("corto", 1)).isInstanceOf(IllegalStateException.class);
    }
}
