package pop3;

import org.apache.commons.net.pop3.POP3Client;
import org.apache.commons.net.pop3.POP3MessageInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class EmailReader {
    private final String server;
    private final String user;
    private final String password;

    public EmailReader(String server, String user, String password) {
        this.server = server;
        this.user = user;
        this.password = password;
    }

    public void readEmails() {
        POP3Client pop3 = new POP3Client();
        try {
            pop3.connect(server, 110);
            if (!pop3.login(user, password)) {
                System.err.println("No se puede conectar. Revisa el usuario y la contraseña.");
                return;
            }

            POP3MessageInfo[] messages = pop3.listMessages();
            if (messages == null) {
                System.err.println("No se pueden recuperar los mensajes.");
                return;
            }
            System.out.println("Respuesta del servidor al comando LIST: " + pop3.getReplyString());
            System.out.println("Numero de mensajes: " + messages.length);

            for (POP3MessageInfo message : messages) {
                Reader reader = pop3.retrieveMessage(message.number);
                BufferedReader br = new BufferedReader(reader);
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
                System.out.println();
            }

            pop3.logout();
            pop3.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java EmailReader <server> <user> <password>");
            return;
        }

        String server = args[0];
        String user = args[1];
        String password = args[2];

        EmailReader reader = new EmailReader(server, user, password);
        try {
            reader.readEmails();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}