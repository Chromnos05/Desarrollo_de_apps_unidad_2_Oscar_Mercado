package co.edu.unicartagena.musicapp.infrastructure.adapter.in.web.dto;

import co.edu.unicartagena.musicapp.domain.model.Usuario;
import jakarta.validation.constraints.NotBlank;

/** DTOs de la tabla Usuario. Nunca se expone el hash de la contraseña. */
public final class UsuarioDtos {

    private UsuarioDtos() { }

    public record UsuarioResponse(Long id, String username, String nombre, String email) {
        public static UsuarioResponse de(Usuario u) {
            return new UsuarioResponse(u.getId(), u.getUsername(), u.getNombre(), u.getEmail());
        }
    }

    public record PerfilRequest(
            @NotBlank(message = "El username es obligatorio") String username,
            @NotBlank(message = "El nombre es obligatorio") String nombre) { }

    public record CambiarPasswordRequest(
            @NotBlank(message = "Escribe tu contraseña actual") String passwordActual,
            @NotBlank(message = "Escribe la nueva contraseña") String nuevaPassword) { }
}
