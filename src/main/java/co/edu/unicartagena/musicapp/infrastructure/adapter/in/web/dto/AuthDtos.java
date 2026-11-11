package co.edu.unicartagena.musicapp.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

/** DTOs de autenticación. */
public final class AuthDtos {

    private AuthDtos() { }

    public record RegistroRequest(
            @NotBlank(message = "El username es obligatorio") String username,
            @NotBlank(message = "El nombre es obligatorio") String nombre,
            @NotBlank(message = "El correo es obligatorio") String email,
            @NotBlank(message = "La contraseña es obligatoria") String password) { }

    /** "usuario" acepta el correo o el username. */
    public record LoginRequest(
            @NotBlank(message = "Escribe tu correo o username") String usuario,
            @NotBlank(message = "Escribe tu contraseña") String password) { }

    public record RecuperarRequest(
            @NotBlank(message = "El correo es obligatorio") String email) { }

    public record RestablecerRequest(
            @NotBlank(message = "El correo es obligatorio") String email,
            @NotBlank(message = "El código es obligatorio") String codigo,
            @NotBlank(message = "La nueva contraseña es obligatoria") String nuevaPassword) { }

    public record SesionResponse(String token, UsuarioDtos.UsuarioResponse usuario) { }
}
