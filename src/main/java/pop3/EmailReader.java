package pop3;

import org.apache.commons.net.pop3.POP3MessageInfo;
import org.apache.commons.net.pop3.POP3SClient;

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
        POP3SClient pop3 = new POP3SClient(true); // Enable implicit SSL
        try {
            pop3.connect("pop.gmail.com", 995);
            if (!pop3.login(user, password)) {
                System.err.println("No se puede conectar. Revisa el usuario y la contraseña.");
                return;
            }

            POP3MessageInfo[] messages = pop3.listMessages();
            if (messages == null) {
                System.err.println("No se pueden recuperar los mensajes.");
                return;
            }

            System.out.println("Numero de mensajes: " + messages.length);

            for (int i = 0; i < messages.length; i++) {
                System.out.println("Mensaje " + (i + 1));
                Reader reader = pop3.retrieveMessage(messages[i].number);
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
}