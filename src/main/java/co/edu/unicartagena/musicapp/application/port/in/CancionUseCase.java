package co.edu.unicartagena.musicapp.application.port.in;

import co.edu.unicartagena.musicapp.domain.model.Cancion;

import java.time.LocalDate;
import java.util.List;

/** Casos de uso: CRUD y reportes de la tabla Canciones. */
public interface CancionUseCase {
    Cancion crear(Cancion cancion);
    Cancion obtener(Long id);
    List<Cancion> listar();
    Cancion actualizar(Long id, Cancion datos);
    void eliminar(Long id);

    // Reportes parametrizados
    List<Cancion> reportePorRitmo(String ritmo);
    List<Cancion> reportePorFechas(LocalDate desde, LocalDate hasta);
    List<Cancion> reportePorDuracion(int minimoSegundos, int maximoSegundos);
}
