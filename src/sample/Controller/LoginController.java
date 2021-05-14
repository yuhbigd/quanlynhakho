package sample.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import sample.ConnectionClass;
import sample.Others.DialogError;
import sample.SceneMap;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class LoginController implements Initializable {


    public static String loggerUsername = "admin";
    public static int loggerAccessLevel;
    @FXML
    private Button login_Button;
    @FXML
    private Label alert_Mess;
    @FXML
    private TextField user_Name;
    @FXML
    private TextField password;
    @FXML
    private ProgressBar progressBar;

    public void loginButtonOnAction(ActionEvent event) {
        if (user_Name.getText().equals("") || password.getText().equals("")) {
            alert_Mess.setText("hãy nhập mật khẩu hoặc tên đăng nhập");
        } else {
            try {
                Connection con = ConnectionClass.getInstances().getConnection();

                String sql = "SELECT * FROM login where user_name = ? and password = ?;";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, user_Name.getText());
                pstmt.setString(2, password.getText());


                ResultSet rsmd = pstmt.executeQuery();
                if (rsmd.next() != false) {
                    alert_Mess.setText("Success");
                    loggerUsername = user_Name.getText();
                    loggerAccessLevel = rsmd.getInt(3);
                    SceneMap.getInstances().changeScene("resources/FXML/Home.fxml");
                } else {
                    new DialogError("Sai tài khoản hoặc mật khẩu");
                }
                login_Button.setDisable(false);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
