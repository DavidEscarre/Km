package cat.copernic.logica;


import cat.copernic.controllers.API.UserApiController;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")  // Carga el correo del remitente desde application.properties
    private String fromEmail;
    
    
    Logger logger = LoggerFactory.getLogger(UserApiController.class);

    public void sendPasswordResetEmail(String to, String token) throws MessagingException {
       
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Recuperación de contraseña");

            // Cuerpo del correo con HTML, usando "cid:logoImage" para referenciar la imagen adjunta
            String htmlContent = "<html>" +
                    "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>" +
                    "<div style='max-width: 600px; margin: auto; background: #363636; padding: 20px; border-radius: 10px; text-align: center;'>" +
                    "<img src='cid:logoImage' alt='Logo de Km+' style='width: 100px; margin-bottom: 20px;'>" +
                    "<h2 style='color: #fff6d6;'>Recuperación de Contraseña</h2>" +
                    "<p style='color: #d4d4d4;'>Has solicitado restablecer tu contraseña. Usa el siguiente código:</p>" +
                    "<h3 style='color: #fff6d6; font-size: 22px; letter-spacing: 0.1em;'>" + token + "</h3>" +
                    "<p style='color: #d4d4d4;'>Si no solicitaste este cambio, ignora este correo.</p>" +
                    "</div>" +
                    "</body></html>";

            helper.setText(htmlContent, true);  // Indicamos que es HTML

            // Adjuntar la imagen desde la carpeta resources
            ClassPathResource logo = new ClassPathResource("emailResources/logo_round.png");
            helper.addInline("logoImage", logo, "image/png");
            
            helper.setText(htmlContent, true);  // Indicamos que es HTML

            mailSender.send(message);
            logger.info("Email enviado con exito a: "+ to);

       
    }
    
   /* public void sendExpirationMessage(String to) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Informacion de cuenta");

            // Cuerpo del correo con HTML, usando "cid:logoImage" para referenciar la imagen adjunta
            String htmlContent = "<html>" +
                    "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>" +
                    "<div style='max-width: 600px; margin: auto; background: white; padding: 20px; border-radius: 10px; text-align: center;'>" +
                    "<img src='cid:logoImage' alt='Logo de AdMe' style='width: 100px; margin-bottom: 20px;'>" +
                    "<h2 style='color: #FFA500;'>Informacion del estado de la cuenta</h2>" +
                    "<p style='color: #333;'>Le informamos que su cuenta de AdMe asociada a este correo ha caducado, reestablezca su contraseña para acceder de nuevo</p>" +
                    "</div>" +
                    "</body></html>";

            helper.setText(htmlContent, true);  // Indicamos que es HTML

            // Adjuntar la imagen desde la carpeta resources
            ClassPathResource logo = new ClassPathResource("/images/ic_logo.png");
            helper.addInline("logoImage", logo);

            mailSender.send(message);
            System.out.println("Correo enviado con éxito");

        } catch (MessagingException e) {
            System.out.println("Error al enviar correo: " + e.getMessage());
        }
    }*/
}
