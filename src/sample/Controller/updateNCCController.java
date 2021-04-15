package sample.Controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.ConnectionClass;
import sample.Others.Congty;
import sample.Others.DialogError;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class updateNCCController implements Initializable {

    @FXML
    private TextField tenTF;

    @FXML
    private TextField dia_chiTF;

    @FXML
    private TextField sdtTf;

    private Connection con;

    @FXML
    void saveAction(ActionEvent event) {
        if(tenTF.getText().length()<1||dia_chiTF.getText().length()<1||sdtTf.getText().length()<1) {
            new DialogError("Không thể để trống các vùng nhập");
        }else{
            String sql = "update ben_ban_hang_cho_kho set ten_cong_ty = ?,dia_chi_cong_ty=?,so_dien_thoai=? where id_cong_ty =?;";
            try {
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1,tenTF.getText());
                pstmt.setString(2,dia_chiTF.getText());
                pstmt.setString(3,sdtTf.getText());
                pstmt.setString(4,nhaCungCapController.congty.getMa_cong_ty());
                pstmt.execute();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            finally {
                HomeController.setChildPane("Resources/FXML/listNhaCungCap.fxml.fxml");
                Stage s = (Stage) tenTF.getScene().getWindow();
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
        sdtTf.setText(nhaCungCapController.congty.getSo_dien_thoai());
        dia_chiTF.setText(nhaCungCapController.congty.getDia_chi());
        tenTF.setText(nhaCungCapController.congty.getTen_cong_ty());
    }
}
