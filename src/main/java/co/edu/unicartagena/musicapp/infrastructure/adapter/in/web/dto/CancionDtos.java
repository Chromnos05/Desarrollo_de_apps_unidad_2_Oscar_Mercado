package co.edu.unicartagena.musicapp.infrastructure.adapter.in.web.dto;

import co.edu.unicartagena.musicapp.domain.model.Cancion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/** DTOs de la tabla Canciones. Las fechas usan el formato AAAA-MM-DD. */
public final class CancionDtos {

    private CancionDtos() { }

    public record CancionRequest(
            @NotBlank(message = "El nombre es obligatorio") String nombre,
            @NotBlank(message = "El ritmo es obligatorio") String ritmo,
            @NotNull(message = "La duración (segundos) es obligatoria") Integer duracion,
            String album,
            Integer posicionEnAlbum,
            String banda,
            String interprete,
            String autor,
            LocalDate fechaLanzamiento) {

        public Cancion aDominio() {
            return new Cancion(null, nombre, ritmo, duracion, album, posicionEnAlbum, banda, interprete, autor,
                    fechaLanzamiento);
        }
    }

    public record CancionResponse(Long id, String nombre, String ritmo, Integer duracion, String album,
                                  Integer posicionEnAlbum, String banda, String interprete, String autor,
                                  LocalDate fechaLanzamiento) {
        public static CancionResponse de(Cancion c) {
            return new CancionResponse(c.getId(), c.getNombre(), c.getRitmo(), c.getDuracion(), c.getAlbum(),
                    c.getPosicionEnAlbum(), c.getBanda(), c.getInterprete(), c.getAutor(), c.getFechaLanzamiento());
        }
    }
}
