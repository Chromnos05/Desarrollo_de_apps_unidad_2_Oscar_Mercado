package co.edu.unicartagena.musicapp.application.port.out;

import co.edu.unicartagena.musicapp.domain.model.Cancion;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** Puerto de salida: persistencia de canciones. */
public interface CancionRepositoryPort {
    Cancion guardar(Cancion cancion);
    Optional<Cancion> buscarPorId(Long id);
    boolean existe(Long id);
    List<Cancion> listar();
    void eliminar(Long id);
    List<Cancion> buscarPorRitmo(String ritmo);
    List<Cancion> buscarPorFechaLanzamientoEntre(LocalDate desde, LocalDate hasta);
    List<Cancion> buscarPorDuracionEntre(int minimo, int maximo);
}
