package co.edu.unicartagena.musicapp.infrastructure.adapter.in.web;

import co.edu.unicartagena.musicapp.application.port.in.CancionUseCase;
import co.edu.unicartagena.musicapp.infrastructure.adapter.in.web.dto.CancionDtos.CancionRequest;
import co.edu.unicartagena.musicapp.infrastructure.adapter.in.web.dto.CancionDtos.CancionResponse;
import co.edu.unicartagena.musicapp.infrastructure.adapter.in.web.dto.RespuestaDtos.MensajeResponse;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/** Adaptador de entrada: CRUD y reportes de la tabla Canciones (requiere token). */
@RestController
@RequestMapping("/api/canciones")
public class CancionController {

    private final CancionUseCase canciones;

    public CancionController(CancionUseCase canciones) {
        this.canciones = canciones;
    }

    @GetMapping
    public List<CancionResponse> listar() {
        return canciones.listar().stream().map(CancionResponse::de).toList();
    }

    @GetMapping("/{id}")
    public CancionResponse obtener(@PathVariable Long id) {
        return CancionResponse.de(canciones.obtener(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CancionResponse crear(@Valid @RequestBody CancionRequest r) {
        return CancionResponse.de(canciones.crear(r.aDominio()));
    }

    @PutMapping("/{id}")
    public CancionResponse actualizar(@PathVariable Long id, @Valid @RequestBody CancionRequest r) {
        return CancionResponse.de(canciones.actualizar(id, r.aDominio()));
    }

    @DeleteMapping("/{id}")
    public MensajeResponse eliminar(@PathVariable Long id) {
        canciones.eliminar(id);
        return new MensajeResponse("Canción eliminada");
    }

    /** Reporte 1: canciones de un ritmo. */
    @GetMapping("/reportes/ritmo")
    public List<CancionResponse> reportePorRitmo(@RequestParam String ritmo) {
        return canciones.reportePorRitmo(ritmo).stream().map(CancionResponse::de).toList();
    }

    /** Reporte 2: canciones lanzadas entre dos fechas (AAAA-MM-DD). */
    @GetMapping("/reportes/fechas")
    public List<CancionResponse> reportePorFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return canciones.reportePorFechas(desde, hasta).stream().map(CancionResponse::de).toList();
    }

    /** Reporte 3: canciones con duración entre min y max segundos. */
    @GetMapping("/reportes/duracion")
    public List<CancionResponse> reportePorDuracion(@RequestParam int min, @RequestParam int max) {
        return canciones.reportePorDuracion(min, max).stream().map(CancionResponse::de).toList();
    }
}
