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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.search.FlagTerm;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;



public class LogOnController {

    private static String login;
    private static String password;


    @FXML
    private TextField loginField;
    @FXML
    private TextField passField;
    @FXML
    private Button singInbtn;

    @FXML
    private Button closeFromButton;


    @FXML
    public void logOn(ActionEvent event) throws IOException {

        if (enter()) {
            Parent mailWindowParent = FXMLLoader.load(getClass().getResource("../view/MainWindow.fxml"));
            Scene mailWindowScene = new Scene(mailWindowParent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.hide();
            stage.setScene(mailWindowScene);
            stage.setResizable(false);
            stage.show();


        }

    }



    public boolean enter() {
        //hideAnimation(singInbtn);
        //showAnimation(loadingcircle);

        login = loginField.getText();
        password = passField.getText();
        boolean success = Main.getInstance().userLogging(login, password);
        if (!success) {
            loginField.setBorder(new Border
                    (new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            return false;
        }

        //hideAnimation(loadingcircle);
        //showAnimation(singInbtn);
        return true;
    }

    @FXML
    public void closeForm() {
        Stage stage = (Stage) closeFromButton.getScene().getWindow();
        stage.close();
    }

}
