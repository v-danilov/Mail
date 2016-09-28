package code;

import javax.mail.AuthenticationFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import java.util.Properties;

/**
 * Created by Bounc on 26.09.2016.
 */
public class User {
    private String username;
    private String password;
    private Session session;
    private Transport transport;

    public boolean authorization(String uname, String pswd){
        username = uname;
        password = pswd;

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.live.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.user", username);
        props.put("mail.smtp.pwd", password);

        try {
            session = Session.getInstance(props);
            session.setDebug(true);
            transport = session.getTransport("smtp");
            try {
                transport.connect("smtp.live.com", 587, username, password);
                //transport.sendMessage(msg, msg.getAllRecipients());
                System.out.println("Connected successfully at " + username);
                //transport.close();
                return true;
            } catch (AuthenticationFailedException afe) {
                System.out.println("Invalid login/password");
            }


        } catch (Exception e) {
            System.err.println("Error: " + e.getStackTrace() + e.getMessage());
        }
        return false;
    }

    public Session getSession() {
        return session;
    }

    public Transport getTransport() {
        return transport;
    }
}
