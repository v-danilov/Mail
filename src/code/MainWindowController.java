package code;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.FlagTerm;
import java.io.File;
import java.io.IOException;

/**
 * Created by Vadim on 19.10.2016.
 */
public class MainWindowController {

    @FXML
    private Label fromLabel;
    @FXML
    private Label subjLabel;
    @FXML
    private TextArea mesField;
    @FXML
    private GridPane pane;

    @FXML
    public void newMessage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/MessageForm.fxml"));
        Parent newMailWindow = loader.load();
        Stage stage = new Stage();
        stage.setTitle("New Message");
        stage.setScene(new Scene(newMailWindow));
        stage.setResizable(false);
        stage.show();

    }

    @FXML
    public void update() {

        Folder mailFolder = Main.getInstance().getFolder();
        if (!mailFolder.isOpen()) {
            try {
                mailFolder.open(Folder.READ_ONLY);
            } catch (Exception e) {
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

            for (int i = len - 1; i >= 0; i--) {
                RowConstraints con = new RowConstraints();
                con.setPrefHeight(80);
                pane.getRowConstraints().add(con);
                froms = messages[i].getFrom();
                adress = froms == null ? null : ((InternetAddress) froms[0]).getAddress();
                pane.add(new Label(adress + "\n" + messages[i].getSubject()), 0, len - i);

            }
            System.out.println(messages.length);
            mailFolder.close(true);
            pane.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
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
                                    String contentType = "";
                                    if(!mailFolder.isOpen()) {
                                        mailFolder.open(Folder.READ_ONLY);
                                        contentType = messages[index].getContentType();
                                    }
                                    else {
                                        contentType = messages[index].getContentType();
                                    }
                                    String messageContent = "";
                                    System.out.println(contentType);
                                    if (contentType.contains("TEXT/plain; charset=us-ascii")
                                            || contentType.contains("TEXT/html; charset=utf-8")
                                            ||contentType.contains("TEXT/html;")) {
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
                                    if (contentType.contains("multipart")) {
                                        Multipart multiPart = (Multipart) messages[index].getContent();
                                        for (int i = 0; i < multiPart.getCount(); i++) {
                                            MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(i);
                                            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()))  {
                                                File attachFolder = new File("C:\\Attachments\\");
                                                if(!attachFolder.exists()){
                                                    attachFolder.mkdir();
                                                }
                                                part.getContent();
                                                System.out.println("Size: " + part.getSize());
                                                part.saveFile(new File(attachFolder.getPath() + File.separator + part.getFileName()));
                                                //part.saveFile(attachFolder.getPath() + File.separator + part.getFileName());
                                            }
                                            else{
                                                messageContent = part.getContent().toString();
                                                mesField.setText(messageContent);
                                            }
                                        }
                                    }
                                    mailFolder.close(true);
                                }
                                catch (Exception e_inner){
                                    System.out.println(e_inner.getMessage() + e_inner.getStackTrace());
                                }
                                //System.out.println( "Node: " + node + " at " + GridPane.getRowIndex( node) + "/" + GridPane.getColumnIndex( node));
                            }
                        }
                    }
                }
            });

        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n" + e.getStackTrace());
        }
    }
}
