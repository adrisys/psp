package smtp;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import java.util.Scanner;

public class EmailSender {
    public static void main(String[] args) {
        // Lee los datos para enviar el correo
        Scanner scanner = new Scanner(System.in);

        System.out.println("Introduce el servidor SMTP:");
        String smtpServer = scanner.nextLine();

        System.out.println("¿Necesita negociación TLS? (true/false):");
        boolean tls = scanner.nextBoolean();
        scanner.nextLine(); // consume newline

        System.out.println("Introduce el puerto:");
        String port = scanner.nextLine();

        System.out.println("Introduce el nombre de usuario:");
        String username = scanner.nextLine();

        System.out.println("Introduce la contraseña:");
        String password = scanner.nextLine();

        System.out.println("Introduce el correo del remitente:");
        String from = scanner.nextLine();

        System.out.println("Introduce el correo del destinatario:");
        String to = scanner.nextLine();

        System.out.println("Introduce el asunto:");
        String subject = scanner.nextLine();

        System.out.println("Introduce el mensaje (termina con *):");
        StringBuilder messageText = new StringBuilder();
        String line;
        while (!(line = scanner.nextLine()).equals("*")) {
            messageText.append(line).append("\n");
        }
        // Envía el correo
        sendEmail(smtpServer, tls, port, username, password, from, to, subject, messageText.toString());
    }

    private static void sendEmail(String smtpServer, boolean tls, String port, String username, String password, String from, String to, String subject, String text) {
        try {
            System.out.println("Estableciendo la sesión con el servidor SMTP...");
            Email email = new SimpleEmail(); // crea un objeto Email
            email.setHostName(smtpServer); // establece el servidor SMTP
            email.setSmtpPort(Integer.parseInt(port)); // establece el puerto
            email.setAuthenticator(new DefaultAuthenticator(username, password)); // establece el usuario y la contraseña
            email.setSSLOnConnect(tls); // establece si se usa TLS
            email.setFrom(from); // establece el remitente
            email.setSubject(subject); // establece el asunto
            email.setMsg(text); // establece el mensaje
            email.addTo(to); // establece el destinatario
            System.out.println("La sesión con el servidor SMTP se ha establecido correctamente.");

            if (text.isEmpty()) {
                System.err.println("El mensaje está vacío. No se puede enviar un mensaje vacío.");
                return;
            }

            System.out.println("Preparando para enviar el mensaje...");
            System.out.println("Enviando el mensaje...");
            email.send();
            System.out.println("Mensaje enviado correctamente");

        } catch (EmailException e) {
            System.out.println("Error al enviar el mensaje: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}