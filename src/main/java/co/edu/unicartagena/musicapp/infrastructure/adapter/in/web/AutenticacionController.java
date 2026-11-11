package co.edu.unicartagena.musicapp.infrastructure.adapter.in.web;

import co.edu.unicartagena.musicapp.application.port.in.AutenticacionUseCase;
import co.edu.unicartagena.musicapp.infrastructure.adapter.in.web.dto.AuthDtos.LoginRequest;
import co.edu.unicartagena.musicapp.infrastructure.adapter.in.web.dto.AuthDtos.RecuperarRequest;
import co.edu.unicartagena.musicapp.infrastructure.adapter.in.web.dto.AuthDtos.RegistroRequest;
import co.edu.unicartagena.musicapp.infrastructure.adapter.in.web.dto.AuthDtos.RestablecerRequest;
import co.edu.unicartagena.musicapp.infrastructure.adapter.in.web.dto.AuthDtos.SesionResponse;
import co.edu.unicartagena.musicapp.infrastructure.adapter.in.web.dto.RespuestaDtos.MensajeResponse;
import co.edu.unicartagena.musicapp.infrastructure.adapter.in.web.dto.UsuarioDtos.UsuarioResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Adaptador de entrada: registro, login y recordatorio de clave por email (rutas públicas). */
@RestController
@RequestMapping("/api/auth")
public class AutenticacionController {

    private final AutenticacionUseCase autenticacion;

    public AutenticacionController(AutenticacionUseCase autenticacion) {
        this.autenticacion = autenticacion;
    }

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public SesionResponse registrar(@Valid @RequestBody RegistroRequest r) {
        var sesion = autenticacion.registrar(
                new AutenticacionUseCase.Registro(r.username(), r.nombre(), r.email(), r.password()));
        return new SesionResponse(sesion.token(), UsuarioResponse.de(sesion.usuario()));
    }

    @PostMapping("/login")
    public SesionResponse login(@Valid @RequestBody LoginRequest r) {
        var sesion = autenticacion.iniciarSesion(r.usuario(), r.password());
        return new SesionResponse(sesion.token(), UsuarioResponse.de(sesion.usuario()));
    }

    @PostMapping("/recuperar")
    public MensajeResponse recuperar(@Valid @RequestBody RecuperarRequest r) {
        autenticacion.solicitarRecuperacion(r.email());
        return new MensajeResponse(
                "Si el correo está registrado, te enviamos un código para restablecer tu contraseña.");
    }

    @PostMapping("/restablecer")
    public MensajeResponse restablecer(@Valid @RequestBody RestablecerRequest r) {
        autenticacion.restablecerPassword(r.email(), r.codigo(), r.nuevaPassword());
        return new MensajeResponse("Tu contraseña fue actualizada. Ya puedes iniciar sesión.");
    }
}
