package co.edu.unicartagena.musicapp.application.port.in;

import co.edu.unicartagena.musicapp.domain.model.Usuario;

import java.util.List;

/** Casos de uso: CRUD y reportes de la tabla Usuario. */
public interface UsuarioUseCase {
    Usuario obtener(Long id);
    List<Usuario> listar();
    Usuario actualizarPerfil(Long id, String username, String nombre);
    void cambiarPassword(Long id, String passwordActual, String nuevaPassword);
    void eliminar(Long id);

    // Reportes parametrizados
    List<Usuario> reportePorUsername(String username);
    List<Usuario> reportePorNombreQueEmpiezaCon(String prefijo);
}
