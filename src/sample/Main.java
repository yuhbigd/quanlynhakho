package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import sample.Others.initialization;

public class Main extends Application {

    public static void main(String[] args) {

        launch(args);
    }

    public void start(Stage myStage) throws Exception {
        new initialization();
        SceneMap.getInstances().setStage(myStage);
        SceneMap.getInstances().setScene("Resources/FXML/LoginFXML.fxml");
        myStage.setResizable(false);

        myStage.show();
    }
}