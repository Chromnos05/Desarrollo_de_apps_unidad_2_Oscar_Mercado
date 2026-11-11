package co.edu.unicartagena.musicapp.domain.model;

import co.edu.unicartagena.musicapp.domain.exception.DatosInvalidosException;

import java.time.LocalDate;

/**
 * Tabla Canciones (id, nombre, ritmo, duración, album, posicionEnAlbum, banda,
 * interprete, autor, fechaLanzamiento). La duración se maneja en segundos.
 */
public class Cancion {

    private Long id;
    private String nombre;
    private String ritmo;
    private Integer duracion;
    private String album;
    private Integer posicionEnAlbum;
    private String banda;
    private String interprete;
    private String autor;
    private LocalDate fechaLanzamiento;

    public Cancion(Long id, String nombre, String ritmo, Integer duracion, String album, Integer posicionEnAlbum,
                   String banda, String interprete, String autor, LocalDate fechaLanzamiento) {
        this.id = id;
        this.nombre = limpiar(nombre);
        this.ritmo = limpiar(ritmo);
        this.duracion = duracion;
        this.album = limpiar(album);
        this.posicionEnAlbum = posicionEnAlbum;
        this.banda = limpiar(banda);
        this.interprete = limpiar(interprete);
        this.autor = limpiar(autor);
        this.fechaLanzamiento = fechaLanzamiento;
        validar();
    }

    private void validar() {
        if (nombre == null || nombre.isEmpty()) {
            throw new DatosInvalidosException("El nombre de la canción es obligatorio");
        }
        if (ritmo == null || ritmo.isEmpty()) {
            throw new DatosInvalidosException("El ritmo es obligatorio");
        }
        if (duracion == null || duracion <= 0) {
            throw new DatosInvalidosException("La duración debe ser un número de segundos mayor que 0");
        }
        if (posicionEnAlbum != null && posicionEnAlbum <= 0) {
            throw new DatosInvalidosException("La posición en el álbum debe ser mayor que 0");
        }
        if (fechaLanzamiento != null && fechaLanzamiento.isAfter(LocalDate.now().plusYears(1))) {
            throw new DatosInvalidosException("La fecha de lanzamiento no es válida");
        }
    }

    /** Copia esta canción con otro id (útil al actualizar). */
    public Cancion conId(Long nuevoId) {
        return new Cancion(nuevoId, nombre, ritmo, duracion, album, posicionEnAlbum, banda, interprete, autor,
                fechaLanzamiento);
    }

    private static String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getRitmo() { return ritmo; }
    public Integer getDuracion() { return duracion; }
    public String getAlbum() { return album; }
    public Integer getPosicionEnAlbum() { return posicionEnAlbum; }
    public String getBanda() { return banda; }
    public String getInterprete() { return interprete; }
    public String getAutor() { return autor; }
    public LocalDate getFechaLanzamiento() { return fechaLanzamiento; }
}
