package co.edu.unicartagena.musicapp.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "usuarios")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "codigo_recuperacion_hash", length = 100)
    private String codigoRecuperacionHash;

    @Column(name = "codigo_recuperacion_expira")
    private Instant codigoRecuperacionExpira;

    @Column(name = "creado_en", insertable = false, updatable = false)
    private Instant creadoEn;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCodigoRecuperacionHash() { return codigoRecuperacionHash; }
    public void setCodigoRecuperacionHash(String codigoRecuperacionHash) { this.codigoRecuperacionHash = codigoRecuperacionHash; }
    public Instant getCodigoRecuperacionExpira() { return codigoRecuperacionExpira; }
    public void setCodigoRecuperacionExpira(Instant codigoRecuperacionExpira) { this.codigoRecuperacionExpira = codigoRecuperacionExpira; }
    public Instant getCreadoEn() { return creadoEn; }
}
