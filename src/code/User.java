package code;

import javax.mail.*;
import java.util.Properties;

/**
 * Created by Bounc on 26.09.2016.
 */
public class User {
    private String username;
    private String password;
    private Session session;
    private Transport transport;
    private Store store;
    private Folder folder;
    static final String ENCODING = "UTF-8";

    public boolean authorization(String uname, String pswd){
        username = uname;
        password = pswd;

        //Свойства подключения к hotmail для отправки сообщений
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.live.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.mime.charset", ENCODING);
        props.put("mail.smtp.user", username);
        props.put("mail.smtp.pwd", password);

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
            }
        };

        try {
            //
            session = Session.getInstance(props, auth);
            session.setDebug(true);
            transport = session.getTransport("smtp");
            try {
                transport.connect("smtp.live.com", 587, username, password);
                System.out.println("Connected successfully at " + username);

                //Прием с помощью pop3
                /*String host = "pop3.live.com";
                Properties pop3Props = new Properties();
                pop3Props.setProperty("mail.pop3s.port",  "995");
                Session rcSession = Session.getInstance(pop3Props, null);
                store = rcSession.getStore("imap");
                store.connect(host, 995, username, password);*/

                String host = "imap-mail.outlook.com";
                Properties imapProps = new Properties();
                imapProps.setProperty("mail.store.protocol", "imaps");
                imapProps.setProperty("mail.imap.ssl.enable", "true");
                imapProps.setProperty("mail.imaps.partialfetch", "false");
                Session rcSession = Session.getInstance(imapProps, null);
                session.setDebug(true);
                store = rcSession.getStore("imap");
                store.connect(host, username, password);
                folder = store.getFolder("INBOX");
                folder.open(Folder.READ_ONLY);

                return true;
            } catch (AuthenticationFailedException afe) {
                //System.out.println("Invalid login/password");
                System.err.println("Error: " + afe.getStackTrace() + afe.getMessage());
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

    public Store getStore() {return store;}

    public Folder getFolder() {
        return folder;
    }
}
