package co.edu.unicartagena.musicapp.infrastructure.adapter.out.persistence.repository;

import co.edu.unicartagena.musicapp.infrastructure.adapter.out.persistence.entity.CancionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CancionJpaRepository extends JpaRepository<CancionEntity, Long> {

    List<CancionEntity> findAllByOrderByNombreAsc();

    List<CancionEntity> findByRitmoIgnoreCaseOrderByNombreAsc(String ritmo);

    List<CancionEntity> findByFechaLanzamientoBetweenOrderByFechaLanzamientoAsc(LocalDate desde, LocalDate hasta);

    List<CancionEntity> findByDuracionBetweenOrderByDuracionAsc(Integer minimo, Integer maximo);
}
