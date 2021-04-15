package sample.Controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.ConnectionClass;
import sample.Others.DialogError;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class updateKhachHangController implements Initializable {

    @FXML
    private TextField ho_tenTF;

    @FXML
    private TextField dia_chiTF;

    @FXML
    private TextField sdtTf;

    private Connection con;

    @FXML
    void okBtn(ActionEvent event) {
        if(ho_tenTF.getText().length()<1||dia_chiTF.getText().length()<1||sdtTf.getText().length()<1) {
            new DialogError("Không thể để trống các vùng nhập");
        }else{
            String sql = "update khach_hang set ten_khach_hang = ?,dia_chi=?,so_dien_thoai=? where ma_khach_hang =?;";
            try {
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1,ho_tenTF.getText());
                pstmt.setString(2,dia_chiTF.getText());
                pstmt.setString(3,sdtTf.getText());
                pstmt.setString(4,KhachhangController.khachhang.getMa_khach());
                pstmt.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            finally {
                HomeController.setChildPane("Resources/FXML/listKhach_hang.fxml.fxml");
                Stage s = (Stage) ho_tenTF.getScene().getWindow();
                s.close();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sdtTf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    sdtTf.setText(newValue.replaceAll("[^\\d]", ""));
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
        sdtTf.setText(KhachhangController.khachhang.getSo_dien_thoai());
        dia_chiTF.setText(KhachhangController.khachhang.getDia_chi());
        ho_tenTF.setText(KhachhangController.khachhang.getTen_khach_hang());
    }
}
