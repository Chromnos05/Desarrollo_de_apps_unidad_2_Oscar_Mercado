package co.edu.unicartagena.musicapp.application.port.out;

import co.edu.unicartagena.musicapp.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

/** Puerto de salida: persistencia de usuarios. */
public interface UsuarioRepositoryPort {
    Usuario guardar(Usuario usuario);
    Optional<Usuario> buscarPorId(Long id);
    Optional<Usuario> buscarPorEmail(String email);
    Optional<Usuario> buscarPorUsername(String username);
    boolean existeEmail(String email);
    boolean existeUsernameEnOtroUsuario(String username, Long idExcluido);
    List<Usuario> listar();
    List<Usuario> buscarPorNombreQueEmpiezaCon(String prefijo);
    void eliminar(Long id);
}
