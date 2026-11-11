package co.edu.unicartagena.musicapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la API REST de MusicApp.
 *
 * Estructura hexagonal:
 *  - domain:          modelos y reglas de negocio (sin dependencias de frameworks)
 *  - application:     puertos (entrada/salida) y casos de uso
 *  - infrastructure:  adaptadores (web, persistencia, seguridad, correo) y configuración
 */
@SpringBootApplication
public class MusicAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusicAppApplication.class, args);
    }
}
