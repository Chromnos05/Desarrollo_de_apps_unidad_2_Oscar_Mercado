package co.edu.unicartagena.musicapp.infrastructure.adapter.in.web;

import co.edu.unicartagena.musicapp.application.port.in.AutenticacionUseCase;
import co.edu.unicartagena.musicapp.domain.exception.CredencialesInvalidasException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Exige el encabezado "Authorization: Bearer <token>" en las rutas protegidas
 * y deja el id del usuario en el atributo "usuarioId" de la petición.
 */
@Component
public class AutenticacionInterceptor implements HandlerInterceptor {

    public static final String ATRIBUTO_USUARIO = "usuarioId";

    private final AutenticacionUseCase autenticacion;

    public AutenticacionInterceptor(AutenticacionUseCase autenticacion) {
        this.autenticacion = autenticacion;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String encabezado = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (encabezado == null || !encabezado.regionMatches(true, 0, "Bearer ", 0, 7)) {
            throw new CredencialesInvalidasException("Debes iniciar sesión (falta el token)");
        }
        Long usuarioId = autenticacion.autenticarToken(encabezado.substring(7).trim());
        request.setAttribute(ATRIBUTO_USUARIO, usuarioId);
        return true;
    }
}
