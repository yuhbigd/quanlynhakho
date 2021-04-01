package sample.Others;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DialogError {
    public DialogError(String alert) {

        Stage stg = new Stage();
        stg.setAlwaysOnTop(true);

        //Modality is so that this window must be interacted before others
        stg.initModality(Modality.APPLICATION_MODAL);
        stg.setResizable(false);

        try {

            Parent root = FXMLLoader.load(getClass().getResource("/sample/Resources/FXML/Dialog.fxml"));
            Scene s = new Scene(root);

            //Getting useful nodes from FXML to set error report
            Label lbl = (Label) root.lookup("#alert");
            Button btn = (Button) root.lookup("#btn");

            lbl.setText(alert);

            //Setting close button event
            btn.setOnAction(event -> {
                stg.close();
            });

            stg.setScene(s);
            stg.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
