package co.edu.unicartagena.musicapp.infrastructure.adapter.in.web;

import co.edu.unicartagena.musicapp.domain.exception.ConflictoException;
import co.edu.unicartagena.musicapp.domain.exception.CredencialesInvalidasException;
import co.edu.unicartagena.musicapp.domain.exception.DatosInvalidosException;
import co.edu.unicartagena.musicapp.domain.exception.RecursoNoEncontradoException;
import co.edu.unicartagena.musicapp.infrastructure.adapter.in.web.dto.RespuestaDtos.ErrorResponse;
import co.edu.unicartagena.musicapp.infrastructure.exception.ServicioNoDisponibleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/** Convierte las excepciones en respuestas JSON con mensajes en español. */
@RestControllerAdvice
public class ManejadorErrores {

    private static final Logger log = LoggerFactory.getLogger(ManejadorErrores.class);

    @ExceptionHandler(DatosInvalidosException.class)
    ResponseEntity<ErrorResponse> datosInvalidos(DatosInvalidosException e) {
        return respuesta(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> validacion(MethodArgumentNotValidException e) {
        String mensaje = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getDefaultMessage())
                .findFirst()
                .orElse("Datos inválidos");
        return respuesta(HttpStatus.BAD_REQUEST, mensaje);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ErrorResponse> jsonInvalido(HttpMessageNotReadableException e) {
        return respuesta(HttpStatus.BAD_REQUEST,
                "El cuerpo de la petición no es un JSON válido (las fechas van en formato AAAA-MM-DD)");
    }

    @ExceptionHandler({MissingRequestValueException.class, MethodArgumentTypeMismatchException.class})
    ResponseEntity<ErrorResponse> parametros(Exception e) {
        return respuesta(HttpStatus.BAD_REQUEST, "Faltan parámetros o tienen un formato incorrecto");
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    ResponseEntity<ErrorResponse> credenciales(CredencialesInvalidasException e) {
        return respuesta(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    ResponseEntity<ErrorResponse> noEncontrado(RecursoNoEncontradoException e) {
        return respuesta(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    ResponseEntity<ErrorResponse> rutaNoExiste(NoResourceFoundException e) {
        return respuesta(HttpStatus.NOT_FOUND, "La ruta solicitada no existe");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    ResponseEntity<ErrorResponse> metodo(HttpRequestMethodNotSupportedException e) {
        return respuesta(HttpStatus.METHOD_NOT_ALLOWED, "Método HTTP no permitido en esta ruta");
    }

    @ExceptionHandler({ConflictoException.class, DataIntegrityViolationException.class})
    ResponseEntity<ErrorResponse> conflicto(RuntimeException e) {
        String mensaje = e instanceof ConflictoException ? e.getMessage() : "El registro ya existe o viola una restricción";
        return respuesta(HttpStatus.CONFLICT, mensaje);
    }

    @ExceptionHandler(ServicioNoDisponibleException.class)
    ResponseEntity<ErrorResponse> servicio(ServicioNoDisponibleException e) {
        return respuesta(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> inesperado(Exception e) {
        log.error("Error inesperado", e);
        return respuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado en el servidor");
    }

    private static ResponseEntity<ErrorResponse> respuesta(HttpStatus estado, String mensaje) {
        return ResponseEntity.status(estado)
                .body(new ErrorResponse(estado.value(), estado.getReasonPhrase(), mensaje));
    }
}
