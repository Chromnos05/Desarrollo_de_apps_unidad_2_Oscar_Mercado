package co.edu.unicartagena.musicapp.infrastructure.adapter.out.persistence;

import co.edu.unicartagena.musicapp.application.port.out.CancionRepositoryPort;
import co.edu.unicartagena.musicapp.domain.model.Cancion;
import co.edu.unicartagena.musicapp.infrastructure.adapter.out.persistence.repository.CancionJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** Adaptador de salida: implementa el puerto de canciones con Spring Data JPA. */
@Component
public class CancionPersistenceAdapter implements CancionRepositoryPort {

    private final CancionJpaRepository jpa;

    public CancionPersistenceAdapter(CancionJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Cancion guardar(Cancion cancion) {
        return PersistenciaMapper.aDominio(jpa.save(PersistenciaMapper.aEntidad(cancion)));
    }

    @Override
    public Optional<Cancion> buscarPorId(Long id) {
        return jpa.findById(id).map(PersistenciaMapper::aDominio);
    }

    @Override
    public boolean existe(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public List<Cancion> listar() {
        return jpa.findAllByOrderByNombreAsc().stream().map(PersistenciaMapper::aDominio).toList();
    }

    @Override
    public void eliminar(Long id) {
        jpa.deleteById(id);
    }

    @Override
    public List<Cancion> buscarPorRitmo(String ritmo) {
        return jpa.findByRitmoIgnoreCaseOrderByNombreAsc(ritmo).stream().map(PersistenciaMapper::aDominio).toList();
    }

    @Override
    public List<Cancion> buscarPorFechaLanzamientoEntre(LocalDate desde, LocalDate hasta) {
        return jpa.findByFechaLanzamientoBetweenOrderByFechaLanzamientoAsc(desde, hasta).stream()
                .map(PersistenciaMapper::aDominio).toList();
    }

    @Override
    public List<Cancion> buscarPorDuracionEntre(int minimo, int maximo) {
        return jpa.findByDuracionBetweenOrderByDuracionAsc(minimo, maximo).stream()
                .map(PersistenciaMapper::aDominio).toList();
    }
}
