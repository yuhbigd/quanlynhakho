package sample.Others;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class View {
    public View(String url) {
        Stage stg = new Stage();

        //Modality is so that this window must be interacted before others
        stg.initModality(Modality.APPLICATION_MODAL);
        try {

            Parent root = FXMLLoader.load(getClass().getResource(url));
            Scene s = new Scene(root);
            s.getStylesheets().add("/sample/Resources/CSS/View.css");
            stg.setScene(s);
            stg.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
