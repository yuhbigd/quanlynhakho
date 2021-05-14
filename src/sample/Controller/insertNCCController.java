package sample.Controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import sample.ConnectionClass;
import sample.Others.DialogError;
import sample.Others.initialization;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class insertNCCController implements Initializable {

    @FXML
    private TextField maCTTF;

    @FXML
    private TextField tenTF;

    @FXML
    private TextField diachiTF;

    @FXML
    private TextField sdtTF;

    private Connection con;

    @FXML
    void okAction(ActionEvent event) {
        if (tenTF.getText().length() < 1 || diachiTF.getText().length() < 1 || sdtTF.getText().length() < 1 || maCTTF.getText().length() < 1) {
            new DialogError("Không thể để trống các vùng nhập");
        } else if (initialization.allCongty.containsKey(maCTTF.getText())) {
            new DialogError("Mã khách hàng không thể trùng");
        } else {
            try {
                con = ConnectionClass.getInstances().getConnection();
                String sql = "insert into ben_ban_hang_cho_kho values(?,?,?,?);";
                PreparedStatement ptsmt = con.prepareStatement(sql);
                ptsmt.setString(1, maCTTF.getText());
                ptsmt.setString(2, tenTF.getText());
                ptsmt.setString(3, diachiTF.getText());
                ptsmt.setString(4, sdtTF.getText());
                ptsmt.execute();
                ptsmt.close();
                new DialogError("Đã thêm nhà cung cấp thành công");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                HomeController.setChildPane("Resources/FXML/listNhaCungCap.fxml");
                Stage s = (Stage) tenTF.getScene().getWindow();
                s.close();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sdtTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    sdtTF.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        try {
            con = ConnectionClass.getInstances().getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        TextFields.bindAutoCompletion(maCTTF, initialization.allCongty.keySet());
    }
}
