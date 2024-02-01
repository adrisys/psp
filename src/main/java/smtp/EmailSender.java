package smtp;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Scanner;

public class EmailSender {
    public static void main(String[] args) {
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

        sendEmail(smtpServer, tls, port, username, password, from, to, subject, messageText.toString());
    }

    private static void sendEmail(String smtpServer, boolean tls, String port, String username, String password, String from, String to, String subject, String text) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", String.valueOf(tls));
        props.put("mail.smtp.host", smtpServer);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);

            System.out.println("Mensaje enviado correctamente");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
