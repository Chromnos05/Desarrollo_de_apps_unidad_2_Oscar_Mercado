package co.edu.unicartagena.musicapp.infrastructure.adapter.out.security;

import co.edu.unicartagena.musicapp.application.port.out.TokenPort;
import co.edu.unicartagena.musicapp.domain.model.Usuario;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

/** Adaptador de salida: emite y valida tokens JWT firmados con HMAC-SHA256. */
@Component
public class JwtTokenAdapter implements TokenPort {

    private static final String EMISOR = "musicapp-api";

    private final SecretKey clave;
    private final Duration duracion;

    public JwtTokenAdapter(@Value("${app.jwt.secret}") String secreto,
                           @Value("${app.jwt.expiracion-horas:24}") long horas) {
        byte[] bytes = secreto.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalStateException("JWT_SECRET debe tener al menos 32 caracteres");
        }
        this.clave = Keys.hmacShaKeyFor(bytes);
        this.duracion = Duration.ofHours(horas);
    }

    @Override
    public String generar(Usuario usuario) {
        Instant ahora = Instant.now();
        return Jwts.builder()
                .issuer(EMISOR)
                .subject(String.valueOf(usuario.getId()))
                .claim("username", usuario.getUsername())
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plus(duracion)))
                .signWith(clave)
                .compact();
    }

    @Override
    public Optional<Long> validar(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        try {
            String sujeto = Jwts.parser()
                    .verifyWith(clave)
                    .requireIssuer(EMISOR)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            return Optional.of(Long.parseLong(sujeto));
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
