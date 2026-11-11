package co.edu.unicartagena.musicapp.infrastructure.adapter.in.web.dto;

/** Respuestas genéricas. */
public final class RespuestaDtos {

    private RespuestaDtos() { }

    public record MensajeResponse(String mensaje) { }

    public record ErrorResponse(int estado, String error, String mensaje) { }
}
