package co.edu.unicartagena.musicapp.application.service;

import co.edu.unicartagena.musicapp.application.port.in.CancionUseCase;
import co.edu.unicartagena.musicapp.application.port.out.CancionRepositoryPort;
import co.edu.unicartagena.musicapp.domain.exception.DatosInvalidosException;
import co.edu.unicartagena.musicapp.domain.exception.RecursoNoEncontradoException;
import co.edu.unicartagena.musicapp.domain.model.Cancion;

import java.time.LocalDate;
import java.util.List;

/** Implementación de los casos de uso de Canciones. No depende de ningún framework. */
public class CancionService implements CancionUseCase {

    private final CancionRepositoryPort repositorio;

    public CancionService(CancionRepositoryPort repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Cancion crear(Cancion cancion) {
        return repositorio.guardar(cancion.conId(null));
    }

    @Override
    public Cancion obtener(Long id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la canción con id " + id));
    }

    @Override
    public List<Cancion> listar() {
        return repositorio.listar();
    }

    @Override
    public Cancion actualizar(Long id, Cancion datos) {
        if (!repositorio.existe(id)) {
            throw new RecursoNoEncontradoException("No existe la canción con id " + id);
        }
        return repositorio.guardar(datos.conId(id));
    }

    @Override
    public void eliminar(Long id) {
        if (!repositorio.existe(id)) {
            throw new RecursoNoEncontradoException("No existe la canción con id " + id);
        }
        repositorio.eliminar(id);
    }

    @Override
    public List<Cancion> reportePorRitmo(String ritmo) {
        if (ritmo == null || ritmo.isBlank()) {
            throw new DatosInvalidosException("Indica el ritmo");
        }
        return repositorio.buscarPorRitmo(ritmo.trim());
    }

    @Override
    public List<Cancion> reportePorFechas(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            throw new DatosInvalidosException("Indica las dos fechas (desde y hasta)");
        }
        if (desde.isAfter(hasta)) {
            throw new DatosInvalidosException("La fecha 'desde' no puede ser posterior a 'hasta'");
        }
        return repositorio.buscarPorFechaLanzamientoEntre(desde, hasta);
    }

    @Override
    public List<Cancion> reportePorDuracion(int minimoSegundos, int maximoSegundos) {
        if (minimoSegundos < 0 || maximoSegundos < minimoSegundos) {
            throw new DatosInvalidosException("El rango de duración no es válido");
        }
        return repositorio.buscarPorDuracionEntre(minimoSegundos, maximoSegundos);
    }
}
