package code;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/**
 * Created by Bounc on 23.09.2016.
 */
public class ControllerNewMessageForm {
    @FXML
    private TextField filedFrom;
    void initialize() {}
    public void setTextToFiled (String text) {
        filedFrom.setText(text);
    }
}
