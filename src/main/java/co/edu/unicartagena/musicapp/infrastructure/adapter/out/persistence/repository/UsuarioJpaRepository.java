package co.edu.unicartagena.musicapp.infrastructure.adapter.out.persistence.repository;

import co.edu.unicartagena.musicapp.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByEmail(String email);

    Optional<UsuarioEntity> findByUsernameIgnoreCase(String username);

    boolean existsByEmail(String email);

    @Query("select count(u) > 0 from UsuarioEntity u where lower(u.username) = lower(:username) "
            + "and (:idExcluido is null or u.id <> :idExcluido)")
    boolean existeUsername(@Param("username") String username, @Param("idExcluido") Long idExcluido);

    @Query("select u from UsuarioEntity u where lower(u.nombre) like lower(concat(:prefijo, '%')) order by u.nombre")
    List<UsuarioEntity> buscarPorNombreQueEmpiezaCon(@Param("prefijo") String prefijo);

    List<UsuarioEntity> findAllByOrderByNombreAsc();
}
