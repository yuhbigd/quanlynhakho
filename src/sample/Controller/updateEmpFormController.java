package sample.Controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.ConnectionClass;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class updateEmpFormController implements Initializable {

    @FXML
    private TextField ho_ten_textF;

    @FXML
    private TextField user_textF;

    @FXML
    private TextField pass_textF;

    @FXML
    private ComboBox<String> chuc_Vu_ComboBox;

    @FXML
    private RadioButton nam_Radio;

    @FXML
    private RadioButton nu_Radio;

    @FXML
    private TextField dien_thoai_textF;

    @FXML
    private TextField noi_O_TextF;
    private String getValueRadio;
    private int chuc_vu;
    private ObservableList<String> observableList = FXCollections.observableArrayList();

    Connection con;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user_textF.setText(listEmpController.emp.getUser_name());
        ho_ten_textF.setText(listEmpController.emp.getHo_ten());
        pass_textF.setText(listEmpController.emp.getPassword());
        dien_thoai_textF.setText(listEmpController.emp.getDien_thoai());
        noi_O_TextF.setText(listEmpController.emp.getNoi_o());
        ToggleGroup group = new ToggleGroup();
        nam_Radio.setToggleGroup(group);
        nu_Radio.setToggleGroup(group);
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {

                if (group.getSelectedToggle() != null) {
                    RadioButton button = (RadioButton) group.getSelectedToggle();
                    getValueRadio = button.getText();
                }
            }
        });

        if(listEmpController.emp.getGioi_tinh().equals("nam")) {
            nam_Radio.setSelected(true);
            getValueRadio = "nam";
        }else {
            nu_Radio.setSelected(true);
            getValueRadio = "nu";
        }
        observableList.add("cửa hàng trưởng");
        observableList.add("nhân viên");
        chuc_Vu_ComboBox.getItems().clear();
        chuc_Vu_ComboBox.setItems(observableList);
        if(listEmpController.emp.getTen_chuc_vu().equals("cua hang truong")){
            chuc_Vu_ComboBox.setValue("cửa hàng trưởng");
            chuc_vu = 1;
        }else {
            chuc_Vu_ComboBox.setValue("nhân viên");
            chuc_vu = 2;
        }
        chuc_Vu_ComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String oldValue, String newValue) {
                     if(newValue.equals("cửa hàng trưởng")){
                         chuc_vu = 1;
                     }else {
                         chuc_vu = 2;
                     }
            }
        });
        dien_thoai_textF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    dien_thoai_textF.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    @FXML
    void saveAction(ActionEvent event) {
        try {
            con = ConnectionClass.getInstances().getConnection();
            String sql = "update login set user_name = ?,password = ?,Chuc_vu_id = ? where user_name = ?;";
            PreparedStatement ptsmt = con.prepareStatement(sql);
            ptsmt.setString(1,user_textF.getText());
            ptsmt.setString(2,pass_textF.getText());
            ptsmt.setInt(3,chuc_vu);
            ptsmt.setString(4,listEmpController.emp.getUser_name());
            ptsmt.executeUpdate();
            ptsmt.close();

            String sql2 = "update thong_tin_chi_tiet\n" +
                    "set ho_ten = ?, dien_thoai = ?,gioi_tinh=?,noi_o=? where  Login_user_name = ?;";
            ptsmt = con.prepareStatement(sql2);
            ptsmt.setString(1,ho_ten_textF.getText());
            ptsmt.setString(2,dien_thoai_textF.getText());
            ptsmt.setString(3,getValueRadio);
            ptsmt.setString(4,noi_O_TextF.getText());
            ptsmt.setString(5,user_textF.getText());
            ptsmt.executeUpdate();

            HomeController.setChildPane("Resources/FXML/listEmp.fxml");
            Stage s =  (Stage)ho_ten_textF.getScene().getWindow();
            s.close();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
