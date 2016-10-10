package code;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;
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
    private TextArea toField;
    @FXML
    private TextArea headField;
    @FXML
    private TextArea bodyField;
    @FXML
    private Label fromLabel;
    @FXML
    private Label subjLabel;
    @FXML
    private TextArea mesField;
    @FXML
    private GridPane pane;
    @FXML
    private Button closeFromButton;

    @FXML
    public void logOn(ActionEvent event) throws IOException {

        if(enter()){
            Parent mailWindowParent = FXMLLoader.load(getClass().getResource("../view/MainWindow.fxml"));
            Scene mailWindowScene = new Scene(mailWindowParent);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.hide();
            stage.setScene(mailWindowScene);
            stage.setResizable(false);
            stage.show();


        }

    }

    @FXML
    public void newMessage() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MessageForm.fxml"));
        Parent newMailWindow = loader.load();
        //ControllerNewMessageForm controllerNewMessageForm = (ControllerNewMessageForm)loader.getController();
        //controllerNewMessageForm.setTextToFiled(login);
        Stage stage = new Stage();
        stage.setTitle("New Message");
        stage.setScene(new Scene(newMailWindow));
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void sendMessage(){
        try {
            Session ssn = Main.getInstance().getSession();
            Transport tport = Main.getInstance().getTransport();
            Message msg = new MimeMessage(ssn);
            msg.setFrom(new InternetAddress(login));
            msg.setSubject(headField.getText());
            msg.setText(bodyField.getText());
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toField.getText()));
            tport.sendMessage(msg, msg.getAllRecipients());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Email successfully sent!");
            alert.showAndWait();
        }
        catch (Exception e){
            System.err.println("Error: " + e.getMessage() + "\n" + e.getStackTrace());
        }
    }

    @FXML
    public void update(){

        Folder mailFolder = Main.getInstance().getFolder();
        if (mailFolder.isOpen() == false){
            try {
                mailFolder.open(Folder.READ_ONLY);
            }
            catch (Exception e){
                System.err.println("Cannot open the folder");
            }
        }
        try {

            FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
            Message[] messages = mailFolder.search(ft);

            Address[] froms;
            String adress;

            //Очистка
            pane.getChildren().removeAll();
            int len = messages.length;

            for (int i = len-1; i >= 0; i--) {
                RowConstraints con = new RowConstraints();
                con.setPrefHeight(80);
                pane.getRowConstraints().add(con);
                froms = messages[i].getFrom();
                adress = froms == null ? null : ((InternetAddress) froms[0]).getAddress();
                pane.add(new Label(adress + "\n" + messages[i].getSubject()),0,len-i);

            }
            System.out.println(messages.length);
            mailFolder.close(true);
            /*pane.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {

                    for( Node node: pane.getChildren()) {

                        if( node instanceof Label) {
                            if( node.getBoundsInParent().contains(e.getSceneX(),  e.getSceneY())) {
                                try {
                                    int index = len - GridPane.getRowIndex(node);
                                    Address[] addresses = messages[index].getFrom();
                                    String from = addresses == null ? null : ((InternetAddress) addresses[0]).getAddress();
                                    fromLabel.setText(from);
                                    String subj = messages[index].getSubject();
                                    subjLabel.setText(subj);
                                    String contentType = messages[index].getContentType();
                                    String messageContent = "";

                                    if (contentType.contains("TEXT/plain; charset=us-ascii")
                                            || contentType.contains("TEXT/html; charset=utf-8")) {
                                        try {
                                            Object content = messages[index].getContent();
                                            if (content != null) {
                                                messageContent = content.toString();
                                            }
                                        } catch (Exception ex) {
                                            messageContent = "[Error downloading content]";
                                            ex.printStackTrace();
                                        }
                                        mesField.setText(messageContent);
                                    }
                                }
                                catch (Exception e_inner){
                                    System.out.println(e_inner.getMessage() + e_inner.getStackTrace());
                                }
                                System.out.println( "Node: " + node + " at " + GridPane.getRowIndex( node) + "/" + GridPane.getColumnIndex( node));
                            }
                        }
                    }
                }
            });*/

        }
        catch (Exception e){
            System.out.println(e.getMessage() + "\n" + e.getStackTrace() );
        }
    }


    public boolean enter() {
        //hideAnimation(singInbtn);
        //showAnimation(loadingcircle);

        login = loginField.getText();
        password = passField.getText();
        boolean success = Main.getInstance().userLogging(login, password);
        if(!success){
            loginField.setBorder(new Border
                    (new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            return false;
        }

        //hideAnimation(loadingcircle);
        //showAnimation(singInbtn);
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

    @FXML
    public void closeForm(){
        Stage stage = (Stage) closeFromButton.getScene().getWindow();
        stage.close();
    }

    private int getRowCount(GridPane pane) {
        int numRows = pane.getRowConstraints().size();
        for (int i = 0; i < pane.getChildren().size(); i++) {
            Node child = pane.getChildren().get(i);
            if (child.isManaged()) {
                Integer rowIndex = GridPane.getRowIndex(child);
                if(rowIndex != null){
                    numRows = Math.max(numRows,rowIndex+1);
                }
            }
        }
        return numRows;
    }
}
