package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import sample.Others.initialization;

public class Main extends Application{

    public void start(Stage myStage) throws Exception {
        SceneMap.getInstances().setStage(myStage);
        new initialization();
        SceneMap.getInstances().setScene("Resources/FXML/Home.fxml");
        myStage.setResizable(false);
        myStage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }
}