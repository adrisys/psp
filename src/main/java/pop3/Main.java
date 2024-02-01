package pop3;

public class Main {
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
