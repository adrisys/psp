package smtp;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Aqui se mandan 10 correos a prueba@localhost desde admin@localhost.
 * Es necesario tener un servidor SMTP en localhost y los usuarios creados,
 * en mi caso he usado Mercury de XAMPP.
 */
public class EmailSenderLocalhost {
    public static void main(String[] args) {
        String to = "prueba@localhost"; // destinatario
        String from = "admin@localhost"; // remitente
        String host = "localhost"; // servidor SMTP de Mercury

        // Configura las propiedades del sistema
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);

        // Obtiene la sesión por defecto
        Session session = Session.getDefaultInstance(properties);
        for (int i = 0; i< 10; i++){
            try {
                // Crea un mensaje por defecto
                MimeMessage message = new MimeMessage(session);

                // Establece el remitente y el destinatario
                message.setFrom(new InternetAddress(from));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

                // Establece el asunto y el cuerpo del mensaje
                message.setSubject("Mensaje de prueba "+i);
                message.setText("Este es el mensaje de prueba "+i);

                // Envia el mensaje
                Transport.send(message);
                System.out.println("Mensaje enviado correctamente");
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }
}