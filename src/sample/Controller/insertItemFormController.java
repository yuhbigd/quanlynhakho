package sample.Controller;

import javafx.beans.property.SimpleStringProperty;
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
import sample.Others.Item;
import sample.Others.View;
import sample.Others.initialization;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class insertItemFormController implements Initializable {

    private final ObservableList<String> observableListType = FXCollections.observableArrayList();
    Connection con;
    @FXML
    private TextField nTF;
    @FXML
    private Label qTextField1;
    @FXML
    private TextField pTF;
    @FXML
    private ComboBox<String> tCB;
    @FXML
    private RadioButton radio_dang_ban;
    @FXML
    private RadioButton radio_dung_ban;
    @FXML
    private Button saveBtn;
    @FXML
    private TextField typeTextField;
    @FXML
    private TextField gia_nhapTF;
    @FXML
    private Button newTypeSaveBtn;
    @FXML
    private TextField bTF;
    private String item_type;
    private String getValueRadio;

    @FXML
    void scanAction(ActionEvent event) {
        scanBarcodeController.barcode = new SimpleStringProperty();
        bTF.textProperty().bindBidirectional(scanBarcodeController.barcode);
        new View("/sample/Resources/FXML/scanBarcode.fxml");
    }

    @FXML
    void saveAction(ActionEvent event) throws SQLException {
        if (bTF.getText().equals("") || nTF.getText().equals("") || pTF.getText().equals("") || item_type.equals("")) {
            new DialogError("không được bỏ trống vùng nhập");
            return;
        }
        if (Double.parseDouble(pTF.getText()) <= 0) {
            new DialogError("giá không thể là số nhỏ hơn 0");
            return;
        }
        for (Item i : initialization.allItem.values()) {
            if (i.getBarcode().equals(bTF.getText())) {
                new DialogError("trùng barcode");
                return;
            }
        }

        int id = 0;

        String sql1 = "select id\n" +
                "from item_type where ten ='" + item_type + "';";

        PreparedStatement ptsmt = con.prepareStatement(sql1);
        ptsmt.executeQuery();
        ResultSet rs = ptsmt.executeQuery();
        while (rs.next()) {
            id = rs.getInt(1);
        }
        ptsmt.close();

        String sql2 = "insert into item values(?,?,?,?,?,?,?);";


        ptsmt = con.prepareStatement(sql2);
        ptsmt.setString(2, nTF.getText());
        ptsmt.setInt(3, 0);
        ptsmt.setDouble(4, Double.parseDouble(pTF.getText()));
        ptsmt.setInt(5, id);
        ptsmt.setString(6, "đang bán");
        ptsmt.setDouble(7, Double.parseDouble(gia_nhapTF.getText()));
        ptsmt.setString(1, bTF.getText());
        ptsmt.execute();
        ptsmt.close();
        initialization.allItem.put(bTF.getText() + " | " + nTF.getText(), new Item(bTF.getText(), nTF.getText(), 0, Double.parseDouble(gia_nhapTF.getText()), tCB.getValue(), getValueRadio, Double.parseDouble(pTF.getText())));
        initialization.subAllItem.put(bTF.getText(), nTF.getText());
        Stage s = (Stage) saveBtn.getScene().getWindow();
        new DialogError("Nhập loại hàng mới thành công");
        s.close();

    }

    @FXML
    void typeSaveBtnAct(ActionEvent event) throws SQLException {
        reload();
        if (typeTextField.getText() != null && !typeTextField.getText().equals("")) {
            if (listAllItemController.item_Type.contains(typeTextField.getText())) {
                new DialogError("loai hang da co trong danh sach");
                newTypeSaveBtn.setVisible(false);
                typeTextField.setVisible(false);
                return;
            }
            // them
            initialization.itemType.add(typeTextField.getText());

            String sql = "SELECT MAX( id ) FROM item_type;";
            PreparedStatement ptsmt = con.prepareStatement(sql);
            ResultSet rs = ptsmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            ptsmt.close();

            String sql1 = "insert into item_type values (?,null,?);";
            ptsmt = con.prepareStatement(sql1);
            ptsmt.setInt(1, count + 1);
            ptsmt.setString(2, typeTextField.getText());
            ptsmt.execute();
            ptsmt.close();
            item_type = typeTextField.getText();
            reload();
            tCB.setValue(typeTextField.getText());


        } else {
            newTypeSaveBtn.setVisible(false);
            typeTextField.setVisible(false);
            new DialogError("khong duoc bo trong vung nhap");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            con = ConnectionClass.getInstances().getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        reload();
        tCB.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String oldValue, String newValue) {
                if (newValue == "them loai moi") {
                    newTypeSaveBtn.setVisible(true);
                    typeTextField.setVisible(true);
                } else {
                    item_type = newValue;
                }
            }
        });

        ToggleGroup group = new ToggleGroup();
        radio_dang_ban.setToggleGroup(group);
        radio_dung_ban.setToggleGroup(group);


        radio_dang_ban.setSelected(true);
        getValueRadio = "đang bán";
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {

                if (group.getSelectedToggle() != null) {
                    RadioButton button = (RadioButton) group.getSelectedToggle();
                    getValueRadio = button.getText();
                }
            }
        });
    }

    public void reload() {
        newTypeSaveBtn.setVisible(false);
        typeTextField.setVisible(false);
        tCB.getItems().clear();
        observableListType.clear();
        observableListType.addAll(initialization.itemType);
        observableListType.add("them loai moi");
        tCB.setItems(observableListType);
    }
}
