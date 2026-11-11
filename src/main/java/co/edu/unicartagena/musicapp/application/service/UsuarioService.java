package co.edu.unicartagena.musicapp.application.service;

import co.edu.unicartagena.musicapp.application.port.in.UsuarioUseCase;
import co.edu.unicartagena.musicapp.application.port.out.PasswordHasherPort;
import co.edu.unicartagena.musicapp.application.port.out.UsuarioRepositoryPort;
import co.edu.unicartagena.musicapp.domain.exception.ConflictoException;
import co.edu.unicartagena.musicapp.domain.exception.CredencialesInvalidasException;
import co.edu.unicartagena.musicapp.domain.exception.DatosInvalidosException;
import co.edu.unicartagena.musicapp.domain.exception.RecursoNoEncontradoException;
import co.edu.unicartagena.musicapp.domain.model.Usuario;

import java.util.List;

/** Implementación de los casos de uso de la tabla Usuario. */
public class UsuarioService implements UsuarioUseCase {

    private final UsuarioRepositoryPort repositorio;
    private final PasswordHasherPort hasher;

    public UsuarioService(UsuarioRepositoryPort repositorio, PasswordHasherPort hasher) {
        this.repositorio = repositorio;
        this.hasher = hasher;
    }

    @Override
    public Usuario obtener(Long id) {
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario con id " + id));
    }

    @Override
    public List<Usuario> listar() {
        return repositorio.listar();
    }

    @Override
    public Usuario actualizarPerfil(Long id, String username, String nombre) {
        Usuario usuario = obtener(id);
        usuario.actualizarPerfil(username, nombre);
        if (repositorio.existeUsernameEnOtroUsuario(usuario.getUsername(), id)) {
            throw new ConflictoException("El username '" + usuario.getUsername() + "' ya está en uso");
        }
        return repositorio.guardar(usuario);
    }

    @Override
    public void cambiarPassword(Long id, String passwordActual, String nuevaPassword) {
        Usuario usuario = obtener(id);
        if (passwordActual == null || !hasher.coincide(passwordActual, usuario.getPasswordHash())) {
            throw new CredencialesInvalidasException("La contraseña actual no es correcta");
        }
        Usuario.validarPassword(nuevaPassword);
        usuario.cambiarPasswordHash(hasher.hash(nuevaPassword));
        repositorio.guardar(usuario);
    }

    @Override
    public void eliminar(Long id) {
        obtener(id);
        repositorio.eliminar(id);
    }

    @Override
    public List<Usuario> reportePorUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new DatosInvalidosException("Indica el username");
        }
        return repositorio.buscarPorUsername(username.trim()).map(List::of).orElse(List.of());
    }

    @Override
    public List<Usuario> reportePorNombreQueEmpiezaCon(String prefijo) {
        if (prefijo == null || prefijo.isBlank()) {
            throw new DatosInvalidosException("Indica el inicio del nombre");
        }
        return repositorio.buscarPorNombreQueEmpiezaCon(prefijo.trim());
    }
}
