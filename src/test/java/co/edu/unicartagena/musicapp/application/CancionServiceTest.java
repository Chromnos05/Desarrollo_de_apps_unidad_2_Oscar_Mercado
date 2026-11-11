package co.edu.unicartagena.musicapp.application;

import co.edu.unicartagena.musicapp.application.port.out.CancionRepositoryPort;
import co.edu.unicartagena.musicapp.application.service.CancionService;
import co.edu.unicartagena.musicapp.domain.exception.DatosInvalidosException;
import co.edu.unicartagena.musicapp.domain.exception.RecursoNoEncontradoException;
import co.edu.unicartagena.musicapp.domain.model.Cancion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CancionServiceTest {

    private CancionRepositoryPort repositorio;
    private CancionService servicio;

    private static Cancion cancion(Long id) {
        return new Cancion(id, "Tusa", "Reggaeton", 200, "Tusa", 1, null, "Karol G", "Karol G",
                LocalDate.of(2019, 11, 7));
    }

    @BeforeEach
    void setUp() {
        repositorio = mock(CancionRepositoryPort.class);
        servicio = new CancionService(repositorio);
    }

    @Test
    void crearIgnoraElIdEnviado() {
        when(repositorio.guardar(any())).thenAnswer(i -> ((Cancion) i.getArgument(0)).conId(1L));
        Cancion creada = servicio.crear(cancion(99L));
        assertThat(creada.getId()).isEqualTo(1L);
    }

    @Test
    void obtenerLanzaErrorSiNoExiste() {
        when(repositorio.buscarPorId(5L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> servicio.obtener(5L)).isInstanceOf(RecursoNoEncontradoException.class);
    }

    @Test
    void actualizarConservaElId() {
        when(repositorio.existe(3L)).thenReturn(true);
        when(repositorio.guardar(any())).thenAnswer(i -> i.getArgument(0));
        assertThat(servicio.actualizar(3L, cancion(null)).getId()).isEqualTo(3L);
    }

    @Test
    void eliminarNoBorraSiNoExiste() {
        when(repositorio.existe(8L)).thenReturn(false);
        assertThatThrownBy(() -> servicio.eliminar(8L)).isInstanceOf(RecursoNoEncontradoException.class);
        verify(repositorio, never()).eliminar(8L);
    }

    @Test
    void reportePorFechasValidaElRango() {
        assertThatThrownBy(() -> servicio.reportePorFechas(LocalDate.of(2020, 1, 2), LocalDate.of(2020, 1, 1)))
                .isInstanceOf(DatosInvalidosException.class);
    }

    @Test
    void reportePorRitmoDelegaEnElRepositorio() {
        when(repositorio.buscarPorRitmo("Reggaeton")).thenReturn(List.of(cancion(1L)));
        assertThat(servicio.reportePorRitmo(" Reggaeton ")).hasSize(1);
    }

    @Test
    void reportePorDuracionValidaElRango() {
        assertThatThrownBy(() -> servicio.reportePorDuracion(300, 100)).isInstanceOf(DatosInvalidosException.class);
    }
}
