package sample.Controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import sample.SceneMap;


import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private Button homeButton;
    @FXML
    private AnchorPane centerPane;

    @FXML
    private Label clock;
    @FXML
    private Button admin;

    @FXML
    private Label username;

    public static Pane childPane = null;

    public static AnchorPane mainPane = null;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(LoginController.loggerAccessLevel == 2) {
            admin.setDisable(true);
        }
        mainPane = centerPane;
        setChildPane("Resources/FXML/Dashboard.fxml");
        //username.setText(LoginController.loggerUsername);

        Runnable clock = new Runnable() {
            @Override
            public void run() {
                runClock();
            }
        };

        Thread newClock = new Thread(clock);
        newClock.setDaemon(true);
        newClock.start();
        username.setText(LoginController.loggerUsername);
    }


    private void runClock() {
        while (true) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    String time = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a"));
                    clock.setText(time);
                }
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void homeButtonOnClick(ActionEvent event) {
       setChildPane("Resources/FXML/Dashboard.fxml");
    }

    public void intvetoryOnClick(ActionEvent event) {
        setChildPane("Resources/FXML/listAllItem.fxml");
    }
    public void sellOnClick(ActionEvent event) {
        setChildPane("Resources/FXML/listXuatHang.fxml");
    }
    public void adminOnClick(ActionEvent event) {
        setChildPane("Resources/FXML/listEmp.fxml");
    }
    @FXML
    void listKH(ActionEvent event) {
        setChildPane("Resources/FXML/listKhach_hang.fxml");
    }

    @FXML
    void listNCC(ActionEvent event) {
        setChildPane("Resources/FXML/listNhaCungCap.fxml");
    }

    @FXML
    void nhapHang(ActionEvent event) {
        setChildPane("Resources/FXML/listNhapHang.fxml");
    }
    public static void setChildPane(String URL){
        AbstractController abs = null;
        try {
            abs = SceneMap.getInstances().getLoader(URL).getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        childPane = abs.getPane();
        childPane.setPrefHeight(mainPane.getPrefHeight());
        childPane.setPrefWidth(mainPane.getPrefWidth());

        mainPane.getChildren().clear();
        mainPane.getChildren().add(childPane);
    }
}
