package co.edu.unicartagena.musicapp.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "canciones")
public class CancionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String ritmo;

    @Column(nullable = false)
    private Integer duracion;

    @Column(length = 150)
    private String album;

    @Column(name = "posicion_en_album")
    private Integer posicionEnAlbum;

    @Column(length = 150)
    private String banda;

    @Column(length = 150)
    private String interprete;

    @Column(length = 150)
    private String autor;

    @Column(name = "fecha_lanzamiento")
    private LocalDate fechaLanzamiento;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRitmo() { return ritmo; }
    public void setRitmo(String ritmo) { this.ritmo = ritmo; }
    public Integer getDuracion() { return duracion; }
    public void setDuracion(Integer duracion) { this.duracion = duracion; }
    public String getAlbum() { return album; }
    public void setAlbum(String album) { this.album = album; }
    public Integer getPosicionEnAlbum() { return posicionEnAlbum; }
    public void setPosicionEnAlbum(Integer posicionEnAlbum) { this.posicionEnAlbum = posicionEnAlbum; }
    public String getBanda() { return banda; }
    public void setBanda(String banda) { this.banda = banda; }
    public String getInterprete() { return interprete; }
    public void setInterprete(String interprete) { this.interprete = interprete; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public LocalDate getFechaLanzamiento() { return fechaLanzamiento; }
    public void setFechaLanzamiento(LocalDate fechaLanzamiento) { this.fechaLanzamiento = fechaLanzamiento; }
}
