package code;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Vadim on 19.10.2016.
 */
public class MessageFormController {

    static final String ENCODING = "UTF-8";
    @FXML
    private TextArea toField;
    @FXML
    private TextArea headField;
    @FXML
    private TextArea bodyField;
    @FXML
    private Button browseButton;
    @FXML
    private Button closeFromButton;

    @FXML
    public void sendMessage() {
        try {
            Session ssn = Main.getInstance().getSession();
            Transport tport = Main.getInstance().getTransport();

            MimeMessage msg = new MimeMessage(ssn);

            msg.setFrom(new InternetAddress("bounce_man@live.com"));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toField.getText()));
            msg.setSubject(headField.getText(), ENCODING);
            //msg.setText(bodyField.getText());

            BodyPart messageBodyPart = new MimeBodyPart();
            //messageBodyPart.setContent(bodyField.getText(), "text/plain; charset=" + ENCODING + "");
            messageBodyPart.setText(bodyField.getText());

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            MimeBodyPart attachmentBodyPart = new MimeBodyPart();

            //File to attach
            String fn = Main.getInstance().getFiles().get(0);
            System.out.println(fn);

            DataSource source = new FileDataSource(fn);
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            attachmentBodyPart.setFileName(MimeUtility.encodeText(source.getName()));
            multipart.addBodyPart(attachmentBodyPart);

            msg.setContent(multipart);
            Transport.send(msg);
            //tport.sendMessage(msg, msg.getAllRecipients());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Email successfully sent!");
            alert.showAndWait();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage() + "\n" + e.getStackTrace());
        }
    }


    @FXML
    public void attachFile() {
        ArrayList<String> attach = new ArrayList<>();
        FileChooser fileChooser = new FileChooser();

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            browseButton.setText(selectedFile.getName());
            attach.add(selectedFile.getAbsolutePath());
            Main.getInstance().setFiles(attach);

        } else {
            browseButton.setText("Browse...");

        }

    }

    @FXML
    public void closeForm() {

        Stage stage = (Stage) closeFromButton.getScene().getWindow();
        stage.close();
    }

}
