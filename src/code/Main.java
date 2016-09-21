package code;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent logOn = FXMLLoader.load(getClass().getResource("../view/LogOn.fxml"));
        primaryStage.setTitle("JMP");
        primaryStage.setScene(new Scene(logOn));

        primaryStage.show();

        //Parent mainWindow = FXMLLoader.load(getClass().getResource("../view/ApplicationWindow.fxml"));
        //primaryStage.setTitle("Hello World");
        //primaryStage.setScene(new Scene(mainWindow, 300, 275));
        //primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
