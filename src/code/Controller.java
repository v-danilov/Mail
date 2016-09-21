package code;


import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;

import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.awt.*;
import java.util.Properties;

public class Controller {

    private String login;
    private String password;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField loginField;
    @FXML
    private TextField passField;
    @FXML
    private Button singInbtn;
    @FXML
    private ProgressIndicator loadingcircle;

    @FXML
    public void logOn() {

        hideAnimation(singInbtn);
        showAnimation(loadingcircle);

        login = loginField.getText();
        password = passField.getText();
        Properties props = null;
        props = new Properties();
        props.put("mail.smtp.host", "smtp.live.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.user", login);
        props.put("mail.smtp.pwd", password);
        try {
            Session session = Session.getInstance(props);
            session.setDebug(true);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(login));
            msg.setSubject("Auto Generated Mail");
            msg.setText("Testing Mail");
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(login));
            Transport transport = session.getTransport("smtp");
            try {
                transport.connect("smtp.live.com", 587, login, password);
                //transport.sendMessage(msg, msg.getAllRecipients());
                System.out.println("Connected successfully at " + login);
                transport.close();
            }
            catch (AuthenticationFailedException afe){
                loginField.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            }

            hideAnimation(loadingcircle);
            showAnimation(singInbtn);


        }
        catch (Exception e){
            System.err.println("Error: " + e.getStackTrace() + e.getMessage());
        }
        singInbtn.setOpacity(1);
    }

    public void hideAnimation(Node node){
        FadeTransition ft = new FadeTransition(Duration.millis(1000), node);
        ft.setFromValue(1.0);
        ft.setToValue(0);
        ft.play();

    }

    public void showAnimation(Node node){
        FadeTransition ft = new FadeTransition(Duration.millis(1000), node);
        ft.setFromValue(0);
        ft.setToValue(1.0);
        ft.play();
    }
}
