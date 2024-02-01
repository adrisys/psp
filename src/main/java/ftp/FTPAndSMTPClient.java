package ftp;
import org.apache.commons.net.ftp.FTPClient;
import pop3.EmailReader;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

// Este programa hace uso de los mismos servidores y usuarios creados con Mercury.
public class FTPAndSMTPClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String username;
        String password;
        int successfulLogins = 0;

        while (!(username = getUsername(scanner)).equals("*")) {
             password = getPassword(scanner);

            if (loginToFTP(username, password)) {
                logConnection(username);
                successfulLogins++;
            }
        }

        sendEmail(successfulLogins);
        System.out.println("Se han enviado los correos.");
        System.out.println();
        System.out.println("Llamamos al metodo readEmails() de la clase EmailReader");
        EmailReader reader = new EmailReader("localhost", "prueba", "prueba");
        try {
            reader.readEmails();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getUsername(Scanner scanner) {
        System.out.println("Introduce el nombre de usuario (o * para terminar):");
        return scanner.nextLine();
    }

    private static String getPassword(Scanner scanner) {
        System.out.println("Introduce la contraseña:");
        return scanner.nextLine();
    }

    private static boolean loginToFTP(String username, String password) {
        FTPClient ftp = new FTPClient();
        try {
            ftp.connect("localhost");
            return ftp.login(username, password);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                ftp.disconnect();
            } catch (IOException ignored) {
            }
        }
    }

    private static void logConnection(String username) {
        try (PrintWriter out = new PrintWriter(new FileWriter("C:\\dam\\psp\\ftp\\" + username + "\\LOG\\LOG.txt", true))) {
            String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            out.println("Usuario " + username + " conectado en: " + timestamp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

private static void sendEmail(int successfulLogins) {
    String to = "prueba@localhost"; // destinatario
    String from = "admin@localhost"; // remitente
    String host = "localhost"; // servidor SMTP de Mercury

    // Configura las propiedades del sistema
    Properties properties = System.getProperties();
    properties.setProperty("mail.smtp.host", host);

    // Obtiene la sesión por defecto
    Session session = Session.getDefaultInstance(properties);

    try {
        // Crea un mensaje por defecto
        MimeMessage message = new MimeMessage(session);

        // Establece el remitente y el destinatario
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

        // Establece el asunto y el cuerpo del mensaje
        message.setSubject("Numero de usuarios logeados");
        message.setText("Se han logeado " + successfulLogins + " usuarios.");

        // Envia el mensaje
        Transport.send(message);
        System.out.println("Mensaje enviado correctamente");
    } catch (MessagingException e) {
        e.printStackTrace();
    }
}
}