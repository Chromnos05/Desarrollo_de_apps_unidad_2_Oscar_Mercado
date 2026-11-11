package co.edu.unicartagena.musicapp.infrastructure.adapter.out.persistence;

import co.edu.unicartagena.musicapp.application.port.out.UsuarioRepositoryPort;
import co.edu.unicartagena.musicapp.domain.model.Usuario;
import co.edu.unicartagena.musicapp.infrastructure.adapter.out.persistence.repository.UsuarioJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/** Adaptador de salida: implementa el puerto de usuarios con Spring Data JPA. */
@Component
public class UsuarioPersistenceAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository jpa;

    public UsuarioPersistenceAdapter(UsuarioJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        return PersistenciaMapper.aDominio(jpa.save(PersistenciaMapper.aEntidad(usuario)));
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return jpa.findById(id).map(PersistenciaMapper::aDominio);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return jpa.findByEmail(email).map(PersistenciaMapper::aDominio);
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        return jpa.findByUsernameIgnoreCase(username).map(PersistenciaMapper::aDominio);
    }

    @Override
    public boolean existeEmail(String email) {
        return jpa.existsByEmail(email);
    }

    @Override
    public boolean existeUsernameEnOtroUsuario(String username, Long idExcluido) {
        return idExcluido == null
                ? jpa.existsByUsernameIgnoreCase(username)
                : jpa.existsByUsernameIgnoreCaseAndIdNot(username, idExcluido);
    }

    @Override
    public List<Usuario> listar() {
        return jpa.findAllByOrderByNombreAsc().stream().map(PersistenciaMapper::aDominio).toList();
    }

    @Override
    public List<Usuario> buscarPorNombreQueEmpiezaCon(String prefijo) {
        return jpa.buscarPorNombreQueEmpiezaCon(prefijo).stream().map(PersistenciaMapper::aDominio).toList();
    }

    @Override
    public void eliminar(Long id) {
        jpa.deleteById(id);
    }
}
