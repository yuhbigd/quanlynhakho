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
import sample.Others.DialogError;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class insertEmpFormController implements Initializable {

    private final ObservableList<String> observableList = FXCollections.observableArrayList();
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
    private Connection con;
    private String getValueRadio;
    private int chuc_vu;

    @FXML
    void saveAction(ActionEvent event) {
        if (ho_ten_textF.getText().equals("") || user_textF.getText().equals("") || pass_textF.getText().equals("") || dien_thoai_textF.getText().equals("")) {
            new DialogError("Khôn được đẻ trống các thông tin");
        } else {
            try {
                con = ConnectionClass.getInstances().getConnection();
                String sql = "insert into login values(?,?,?);";
                PreparedStatement ptsmt = con.prepareStatement(sql);
                ptsmt.setString(1, user_textF.getText());
                ptsmt.setString(2, pass_textF.getText());
                ptsmt.setInt(3, chuc_vu);
                ptsmt.execute();
                ptsmt.close();
                String sql2 = "insert into thong_tin_chi_tiet values(?,null,?,?,?,?);";
                ptsmt = con.prepareStatement(sql2);
                ptsmt.setString(1, user_textF.getText());
                ptsmt.setString(2, noi_O_TextF.getText());
                ptsmt.setString(3, getValueRadio);
                ptsmt.setString(4, dien_thoai_textF.getText());
                ptsmt.setString(5, ho_ten_textF.getText());
                ptsmt.execute();
                ptsmt.close();

                new DialogError("Đã thêm nhân viên thành công");
                HomeController.setChildPane("Resources/FXML/listEmp.fxml");
                Stage s = (Stage) ho_ten_textF.getScene().getWindow();
                s.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        nam_Radio.setSelected(true);
        getValueRadio = "nam";
        observableList.add("cửa hàng trưởng");
        observableList.add("nhân viên");
        chuc_Vu_ComboBox.getItems().clear();
        chuc_Vu_ComboBox.setItems(observableList);
        chuc_Vu_ComboBox.setValue("cửa hàng trưởng");
        chuc_vu = 1;
        chuc_Vu_ComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String oldValue, String newValue) {
                if (newValue.equals("cửa hàng trưởng")) {
                    chuc_vu = 1;
                } else {
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
}
