package co.edu.unicartagena.musicapp.infrastructure.adapter.in.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/** Verificación de que la API está en línea. */
@RestController
public class EstadoController {

    @GetMapping({"/", "/api/health"})
    public Map<String, Object> estado() {
        return Map.of(
                "servicio", "MusicApp API",
                "estado", "ok",
                "hora", Instant.now().toString());
    }
}
