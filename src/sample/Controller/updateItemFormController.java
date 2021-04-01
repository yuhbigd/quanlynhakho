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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class updateItemFormController implements Initializable {
    @FXML
    private Label bTF;

    @FXML
    private TextField nTF;

    @FXML
    private Label qTextField;

    @FXML
    private TextField qTF;

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

    private String getValueRadio;

    @FXML
    private Button saveBtn;


    private  ObservableList<String> observableListType = FXCollections.observableArrayList();

    @FXML
    private TextField typeTextField;

    @FXML
    private Button newTypeSaveBtn;

    private Connection con;

    private String item_type;
    int id = 0;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        item_type = listAllItemController.item.getItem_type();
        try {
            con = ConnectionClass.getInstances().getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        reload();

        tCB.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String oldValue, String newValue) {
                if(newValue == "them loai moi"){
                    newTypeSaveBtn.setVisible(true);
                    typeTextField.setVisible(true);
                }else{
                    item_type = newValue;
                }
            }
        });

        ToggleGroup group = new ToggleGroup();
        radio_dang_ban.setToggleGroup(group);
        radio_dung_ban.setToggleGroup(group);


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



    @FXML
    void saveAction(ActionEvent event) throws SQLException {
        update();
        HomeController.setChildPane("Resources/FXML/listAllItem.fxml");
        Stage s =  (Stage)saveBtn.getScene().getWindow();
        s.close();
    }


    private void update() throws SQLException{
        PreparedStatement ptsmt = null;
        if( Integer.parseInt(qTF.getText()) < 0 ||Double.parseDouble(qTF.getText()) < 0 ){
            new DialogError("Gia ban va so luong khong the am");
            return;
        }

        String sql1 = "select id\n" +
                "from item_type where ten ='" +item_type+"';";

        ptsmt = con.prepareStatement(sql1);
        ptsmt.executeQuery();
        ResultSet rs = ptsmt.executeQuery();
        while(rs.next()) {
            id = rs.getInt(1);
        }


        ptsmt.close();

        String sql2 = "update  item \n" +
                "set item_name=?,so_luong=?,gia_ban=?,Item_type_id = ?,trang_thai=? where barcode = ?;";


        ptsmt = con.prepareStatement(sql2);
        ptsmt.setString(1,nTF.getText());
        ptsmt.setInt(2,Integer.parseInt(qTF.getText()));
        ptsmt.setDouble(3,Double.parseDouble(pTF.getText()));
        ptsmt.setInt(4,id);
        ptsmt.setString(5,getValueRadio);
        ptsmt.setString(6,bTF.getText());
        ptsmt.executeUpdate();


        ptsmt.close();
    }

    @FXML
    void typeSaveBtnAct(ActionEvent event) throws SQLException, ClassNotFoundException {
        if(typeTextField.getText() != null && !typeTextField.getText().equals("") ){
            if(listAllItemController.item_Type.contains(typeTextField.getText())){
                new DialogError("loai hang da co trong danh sach");
                newTypeSaveBtn.setVisible(false);
                typeTextField.setVisible(false);
                return;
            }
            // them
            listAllItemController.item_Type.add(typeTextField.getText());

                String sql = "SELECT MAX( id ) FROM item_type;";
                PreparedStatement ptsmt = con.prepareStatement(sql);
               ResultSet rs = ptsmt.executeQuery();
               int count = 0;
               while (rs.next()){
                  count = rs.getInt(1);
               }
               ptsmt.close();

                String sql1 = "insert into item_type values (?,null,?);";
               ptsmt = con.prepareStatement(sql1);
               ptsmt.setInt(1,count+1);
               ptsmt.setString(2,typeTextField.getText());
               ptsmt.execute();
               ptsmt.close();

            reload();
            tCB.setValue(typeTextField.getText());


        }else  {
            newTypeSaveBtn.setVisible(false);
            typeTextField.setVisible(false);
            new DialogError("khong duoc bo trong vung nhap");
        }
    }

    public void reload() {
        newTypeSaveBtn.setVisible(false);
        typeTextField.setVisible(false);
        tCB.getItems().clear();
        observableListType.clear();
        observableListType.addAll(listAllItemController.item_Type);
        observableListType.add("them loai moi");
        tCB.setItems(observableListType);
        tCB.setValue(listAllItemController.item.getItem_type());
        if(listAllItemController.item.getTrang_thai().equals("dang ban")) {
            radio_dang_ban.setSelected(true);
            getValueRadio = "dang ban";
        }else {
            radio_dung_ban.setSelected(true);
            getValueRadio = "dung ban";
        }


        bTF.setText(listAllItemController.item.getBarcode());
        nTF.setText(listAllItemController.item.getItem_name());
        qTF.setText(Integer.toString(listAllItemController.item.getSo_luong()));
        pTF.setText(Double.toString(listAllItemController.item.getGia_ban()));

    }
}
