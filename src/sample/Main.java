package sample;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{

    public void start(Stage myStage) throws Exception {
        SceneMap.getInstances().setStage(myStage);
        SceneMap.getInstances().setScene("Resources/FXML/LoginFXMl.fxml");
        myStage.setResizable(false);
        myStage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }
}