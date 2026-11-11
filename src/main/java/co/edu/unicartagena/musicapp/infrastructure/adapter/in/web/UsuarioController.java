package co.edu.unicartagena.musicapp.infrastructure.adapter.in.web;

import co.edu.unicartagena.musicapp.application.port.in.UsuarioUseCase;
import co.edu.unicartagena.musicapp.infrastructure.adapter.in.web.dto.RespuestaDtos.MensajeResponse;
import co.edu.unicartagena.musicapp.infrastructure.adapter.in.web.dto.UsuarioDtos.CambiarPasswordRequest;
import co.edu.unicartagena.musicapp.infrastructure.adapter.in.web.dto.UsuarioDtos.PerfilRequest;
import co.edu.unicartagena.musicapp.infrastructure.adapter.in.web.dto.UsuarioDtos.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Adaptador de entrada: CRUD y reportes de la tabla Usuario (requiere token). */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private static final String USUARIO = AutenticacionInterceptor.ATRIBUTO_USUARIO;

    private final UsuarioUseCase usuarios;

    public UsuarioController(UsuarioUseCase usuarios) {
        this.usuarios = usuarios;
    }

    @GetMapping
    public List<UsuarioResponse> listar() {
        return usuarios.listar().stream().map(UsuarioResponse::de).toList();
    }

    @GetMapping("/me")
    public UsuarioResponse miPerfil(@RequestAttribute(USUARIO) Long usuarioId) {
        return UsuarioResponse.de(usuarios.obtener(usuarioId));
    }

    @PutMapping("/me")
    public UsuarioResponse actualizar(@RequestAttribute(USUARIO) Long usuarioId,
                                      @Valid @RequestBody PerfilRequest r) {
        return UsuarioResponse.de(usuarios.actualizarPerfil(usuarioId, r.username(), r.nombre()));
    }

    @PutMapping("/me/password")
    public MensajeResponse cambiarPassword(@RequestAttribute(USUARIO) Long usuarioId,
                                           @Valid @RequestBody CambiarPasswordRequest r) {
        usuarios.cambiarPassword(usuarioId, r.passwordActual(), r.nuevaPassword());
        return new MensajeResponse("Contraseña actualizada");
    }

    @DeleteMapping("/me")
    public MensajeResponse eliminar(@RequestAttribute(USUARIO) Long usuarioId) {
        usuarios.eliminar(usuarioId);
        return new MensajeResponse("Cuenta eliminada");
    }

    /** Reporte 1: usuario por username exacto. */
    @GetMapping("/reportes/username")
    public List<UsuarioResponse> reportePorUsername(@RequestParam String valor) {
        return usuarios.reportePorUsername(valor).stream().map(UsuarioResponse::de).toList();
    }

    /** Reporte 2: usuarios cuyo nombre empieza por un texto. */
    @GetMapping("/reportes/nombre")
    public List<UsuarioResponse> reportePorNombre(@RequestParam String prefijo) {
        return usuarios.reportePorNombreQueEmpiezaCon(prefijo).stream().map(UsuarioResponse::de).toList();
    }
}
