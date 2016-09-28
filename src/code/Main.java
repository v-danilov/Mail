package code;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.mail.Session;
import javax.mail.Transport;

public class Main extends Application {

    private User user;

    private static Main instance;

    public Main() {
        instance = this;
    }

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent logOn = FXMLLoader.load(getClass().getResource("../view/LogOn.fxml"));
        primaryStage.setTitle("JMP");
        primaryStage.setScene(new Scene(logOn));
        primaryStage.setResizable(false);
        primaryStage.show();

        //Parent mainWindow = FXMLLoader.load(getClass().getResource("../view/MessageForm.fxml"));
        //primaryStage.setTitle("Hello World");
        //primaryStage.setScene(new Scene(mainWindow, 300, 275));
        //primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public boolean userLogging(String u, String p){
        user = new User();
        boolean bool = user.authorization(u,p);
        return  bool;
    }

    public Session getSession(){
        return user.getSession();
    }

    public Transport getTransport(){
        return user.getTransport();
    }

}
