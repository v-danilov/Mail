package code;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.event.ActionEvent;
import java.io.IOException;

public class Controller {

    private static String login;
    private static String password;


    @FXML
    private TextField loginField;
    @FXML
    private TextField passField;
    @FXML
    private Button singInbtn;
    @FXML
    private ProgressIndicator loadingcircle;
    @FXML
    private TextField filedFrom;
    @FXML
    private TextField filedTo;
    @FXML
    private TextField mesBody;

    @FXML
    public void logOn(ActionEvent event) throws IOException {

        if(enter()){
            Parent mailWindowParent = FXMLLoader.load(getClass().getResource("../view/MailWindow.fxml"));
            Scene mailWindowScene = new Scene(mailWindowParent);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.hide();
            stage.setScene(mailWindowScene);
            stage.show();
        }

    }

    @FXML
    public void newMessage(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/NewMessageForm.fxml"));
        Parent newMailWindow = loader.load();
        //ControllerNewMessageForm controllerNewMessageForm = (ControllerNewMessageForm)loader.getController();
        //controllerNewMessageForm.setTextToFiled(login);
        Stage stage = new Stage();
        stage.setTitle("New Message");
        stage.setScene(new Scene(newMailWindow));
        stage.show();
    }

    @FXML
    public void sendMessage(){
        try {
            /*Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(login));
            msg.setSubject("Auto Generated Mail");
            msg.setText(mesBody.getText());
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(filedTo.getText()));
            transport.sendMessage(msg, msg.getAllRecipients());*/
        }
        catch (Exception e){
            System.err.println("Error: " + e.getMessage() + "\n" + e.getStackTrace());
        }
    }

    public void hideAnimation(Node node) {
        node.setDisable(true);
        node.setOpacity(0);
    }

    public void showAnimation(Node node) {
        node.setDisable(false);
        node.setOpacity(1);
    }

    public boolean enter() {
        hideAnimation(singInbtn);
        showAnimation(loadingcircle);

        login = loginField.getText();
        password = passField.getText();
        boolean success = Main.getInstance().userLogging(login,password);
        if(!success){
            loginField.setBorder(new Border
                    (new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            return false;
        }

        hideAnimation(loadingcircle);
        showAnimation(singInbtn);
        return true;
       /* Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.live.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.user", login);
        props.put("mail.smtp.pwd", password);
        try {
            session = Session.getInstance(props);
            session.setDebug(true);
            transport = session.getTransport("smtp");
            try {
                transport.connect("smtp.live.com", 587, login, password);
                //transport.sendMessage(msg, msg.getAllRecipients());
                System.out.println("Connected successfully at " + login);
                //transport.close();
                return true;
            } catch (AuthenticationFailedException afe) {
                loginField.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            }

            hideAnimation(loadingcircle);
            showAnimation(singInbtn);


        } catch (Exception e) {
            System.err.println("Error: " + e.getStackTrace() + e.getMessage());
        }
        return false;*/

    }
}
