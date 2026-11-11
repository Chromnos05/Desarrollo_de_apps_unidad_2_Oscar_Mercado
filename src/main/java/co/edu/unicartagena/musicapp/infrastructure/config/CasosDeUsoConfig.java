package co.edu.unicartagena.musicapp.infrastructure.config;

import co.edu.unicartagena.musicapp.application.port.in.AutenticacionUseCase;
import co.edu.unicartagena.musicapp.application.port.in.CancionUseCase;
import co.edu.unicartagena.musicapp.application.port.in.UsuarioUseCase;
import co.edu.unicartagena.musicapp.application.port.out.CancionRepositoryPort;
import co.edu.unicartagena.musicapp.application.port.out.NotificacionPort;
import co.edu.unicartagena.musicapp.application.port.out.PasswordHasherPort;
import co.edu.unicartagena.musicapp.application.port.out.TokenPort;
import co.edu.unicartagena.musicapp.application.port.out.UsuarioRepositoryPort;
import co.edu.unicartagena.musicapp.application.service.AutenticacionService;
import co.edu.unicartagena.musicapp.application.service.CancionService;
import co.edu.unicartagena.musicapp.application.service.UsuarioService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Clock;

/**
 * Conecta los casos de uso (núcleo de la aplicación) con los adaptadores.
 * Los servicios no tienen anotaciones de Spring: el framework solo vive en la infraestructura.
 */
@Configuration
@EnableTransactionManagement
public class CasosDeUsoConfig {

    @Bean
    Clock reloj() {
        return Clock.systemUTC();
    }

    @Bean
    CancionUseCase cancionUseCase(CancionRepositoryPort repositorio) {
        return new CancionService(repositorio);
    }

    @Bean
    UsuarioUseCase usuarioUseCase(UsuarioRepositoryPort repositorio, PasswordHasherPort hasher) {
        return new UsuarioService(repositorio, hasher);
    }

    @Bean
    AutenticacionUseCase autenticacionUseCase(UsuarioRepositoryPort repositorio, PasswordHasherPort hasher,
                                              TokenPort tokens, NotificacionPort notificaciones, Clock reloj,
                                              @Value("${app.recuperacion.minutos-validez:15}") int minutos) {
        return new AutenticacionService(repositorio, hasher, tokens, notificaciones, reloj, minutos);
    }
}
