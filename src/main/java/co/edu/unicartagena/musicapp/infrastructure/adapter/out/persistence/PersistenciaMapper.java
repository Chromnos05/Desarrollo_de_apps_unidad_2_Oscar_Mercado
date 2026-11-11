package co.edu.unicartagena.musicapp.infrastructure.adapter.out.persistence;

import co.edu.unicartagena.musicapp.domain.model.Cancion;
import co.edu.unicartagena.musicapp.domain.model.Usuario;
import co.edu.unicartagena.musicapp.infrastructure.adapter.out.persistence.entity.CancionEntity;
import co.edu.unicartagena.musicapp.infrastructure.adapter.out.persistence.entity.UsuarioEntity;

/** Convierte entre los modelos del dominio y las entidades JPA. */
final class PersistenciaMapper {

    private PersistenciaMapper() { }

    static Usuario aDominio(UsuarioEntity e) {
        return new Usuario(e.getId(), e.getUsername(), e.getNombre(), e.getEmail(), e.getPasswordHash(),
                e.getCodigoRecuperacionHash(), e.getCodigoRecuperacionExpira());
    }

    static UsuarioEntity aEntidad(Usuario u) {
        UsuarioEntity e = new UsuarioEntity();
        e.setId(u.getId());
        e.setUsername(u.getUsername());
        e.setNombre(u.getNombre());
        e.setEmail(u.getEmail());
        e.setPasswordHash(u.getPasswordHash());
        e.setCodigoRecuperacionHash(u.getCodigoRecuperacionHash());
        e.setCodigoRecuperacionExpira(u.getCodigoRecuperacionExpira());
        return e;
    }

    static Cancion aDominio(CancionEntity e) {
        return new Cancion(e.getId(), e.getNombre(), e.getRitmo(), e.getDuracion(), e.getAlbum(),
                e.getPosicionEnAlbum(), e.getBanda(), e.getInterprete(), e.getAutor(), e.getFechaLanzamiento());
    }

    static CancionEntity aEntidad(Cancion c) {
        CancionEntity e = new CancionEntity();
        e.setId(c.getId());
        e.setNombre(c.getNombre());
        e.setRitmo(c.getRitmo());
        e.setDuracion(c.getDuracion());
        e.setAlbum(c.getAlbum());
        e.setPosicionEnAlbum(c.getPosicionEnAlbum());
        e.setBanda(c.getBanda());
        e.setInterprete(c.getInterprete());
        e.setAutor(c.getAutor());
        e.setFechaLanzamiento(c.getFechaLanzamiento());
        return e;
    }
}
