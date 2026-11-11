package co.edu.unicartagena.musicapp.infrastructure.adapter.out.mail;

import co.edu.unicartagena.musicapp.application.port.out.NotificacionPort;
import co.edu.unicartagena.musicapp.infrastructure.exception.ServicioNoDisponibleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Adaptador de salida: envía el código de recuperación por SMTP.
 * Si no hay servidor de correo configurado (desarrollo local), el código se escribe en el log.
 */
@Component
public class SmtpNotificacionAdapter implements NotificacionPort {

    private static final Logger log = LoggerFactory.getLogger(SmtpNotificacionAdapter.class);

    private final ObjectProvider<JavaMailSender> mailSender;
    private final String host;
    private final String remitente;

    public SmtpNotificacionAdapter(ObjectProvider<JavaMailSender> mailSender,
                                   @Value("${spring.mail.host:}") String host,
                                   @Value("${app.mail.remitente}") String remitente) {
        this.mailSender = mailSender;
        this.host = host;
        this.remitente = remitente;
    }

    @Override
    public void enviarCodigoRecuperacion(String email, String nombre, String codigo, int minutosValidez) {
        JavaMailSender sender = mailSender.getIfAvailable();
        if (host == null || host.isBlank() || sender == null) {
            log.warn("Correo no configurado (MAIL_HOST vacío). Código de recuperación para {}: {}", email, codigo);
            return;
        }
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(remitente);
        mensaje.setTo(email);
        mensaje.setSubject("MusicApp - Código para restablecer tu contraseña");
        mensaje.setText("""
                Hola %s,

                Recibimos una solicitud para restablecer la contraseña de tu cuenta en MusicApp.

                Tu código de verificación es: %s

                Escríbelo en la app junto con tu nueva contraseña. El código vence en %d minutos.
                Si no solicitaste este cambio, ignora este correo: tu contraseña actual sigue funcionando.

                — Equipo MusicApp
                """.formatted(nombre, codigo, minutosValidez));
        try {
            sender.send(mensaje);
        } catch (MailException e) {
            log.error("No se pudo enviar el correo de recuperación a {}", email, e);
            throw new ServicioNoDisponibleException("No se pudo enviar el correo. Intenta más tarde.", e);
        }
    }
}
