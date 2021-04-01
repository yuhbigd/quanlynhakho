package sample.Controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.textfield.TextFields;
import sample.ConnectionClass;
import sample.Others.DialogError;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SellController extends AbstractController implements Initializable {

    @FXML
    private TextField barTextField;

    @FXML
    private TextField quanTextField;

    @FXML
    private Label moneyReceivedLabel;

    @FXML
    private AnchorPane pane;

    @FXML
    private TextField moneyReceivedTF;



    @FXML
    private Label priceLabel;

    @FXML
    private Button verifyBtn;

    @FXML
    private Button completeBtn;

    @FXML
    private Label totalComeLabel;
    @FXML
    private Button cancelButton;

    private Map<String,Integer> quantity_Map;
    private Map<String,Double> price_Map;
    private ArrayList<String> item_Name_Array;
    private Map<String,Double> gia_nhap_Map;

    private String bcode;

    private double totalReceived;
    Connection con;
    @FXML
    void Complete(ActionEvent event) {
        String moneyR = moneyReceivedTF.getText();
        if(moneyR.equals("")){
            new DialogError("So tien nhan khong the de trong");
            return;
        }else {
            double money = Double.parseDouble(moneyReceivedTF.getText());
            if(money < totalReceived || money <= 0) {
                new DialogError("So tien nhan khong the nho hon so tien phai tra");
                return;
            }
            double charge = money - totalReceived;

            PreparedStatement pstmt;
            try {
                String sql = "insert into ban_hang values(null,?,?,?,?,now(),?,?,?);";
                pstmt = con.prepareStatement(sql);
                pstmt.setString(1,bcode);
                pstmt.setString(2,LoginController.loggerUsername);
                pstmt.setInt(3,Integer.parseInt(quanTextField.getText()));
                pstmt.setDouble(4,Double.parseDouble(priceLabel.getText()));
                pstmt.setDouble(5,totalReceived);
                pstmt.setDouble(6,charge);
                pstmt.setDouble(7,gia_nhap_Map.get(bcode));
                pstmt.execute();
                pstmt.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                String sql = "update item set so_luong = so_luong - ? where barcode = ?;";
                pstmt = con.prepareStatement(sql);
                pstmt.setInt(1,Integer.parseInt(quanTextField.getText()));
                pstmt.setString(2,bcode);
                pstmt.executeUpdate();
                pstmt.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            new DialogError("Số tiền phải trả lại là : "+ Double.toString(charge));
            moneyReceivedLabel.setVisible(false);
            moneyReceivedTF.setVisible(false);
            completeBtn.setVisible(false);


            barTextField.setDisable(false);
            quanTextField.setDisable(false);
            return;
        }
    }

    @FXML
    void Verify(ActionEvent event) {
        if(barTextField.getText().equals("")||!item_Name_Array.contains(barTextField.getText())){
            new DialogError("Hay xem lai phan barcode cua san pham");
            return;
        }else{
            String[] barcode = barTextField.getText().split(" | ");
            bcode = barcode[0];
            String quantity = quanTextField.getText();
            if(quantity_Map.get(barcode[0]) == 0) {
                new DialogError("con 0 san pham trong kho");
                return;
            }
            if(quantity.equals("")||Integer.parseInt(quantity) > quantity_Map.get(barcode[0])){
                new DialogError("Khong du san pham de ban");
                return;
            }
            priceLabel.setText(Double.toString(price_Map.get(barcode[0])));
            totalReceived = price_Map.get(barcode[0])*Integer.parseInt(quantity);
            totalComeLabel.setText(Double.toString(totalReceived));


            moneyReceivedLabel.setVisible(true);
            moneyReceivedTF.setVisible(true);
            completeBtn.setVisible(true);


            barTextField.setDisable(true);
            quanTextField.setDisable(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        moneyReceivedLabel.setVisible(false);
        moneyReceivedTF.setVisible(false);
        completeBtn.setVisible(false);

        quantity_Map = new HashMap<>();
        price_Map = new HashMap<>();
        item_Name_Array = new ArrayList<>();
        gia_nhap_Map = new HashMap<>();

        try {
            con = ConnectionClass.getInstances().getConnection();
            load();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        TextFields.bindAutoCompletion(barTextField,item_Name_Array);

        quanTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    quanTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        moneyReceivedTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    moneyReceivedTF.setText(oldValue);
                }
            }
        });
    }

    public void load() throws SQLException {
        String sql = "SELECT * FROM item where  trang_thai = 'dang ban';";
        PreparedStatement pstmt = con.prepareStatement(sql);

        ResultSet rs  = pstmt.executeQuery();
        String set;
        while (rs.next()) {
            set = rs.getString("barcode") + " | "+rs.getString("item_name");
            //item_Barcode.add(rs.getString("barcode"));
            item_Name_Array.add(set);
            price_Map.put(rs.getString("barcode"),rs.getDouble("gia_ban"));
            quantity_Map.put(rs.getString("barcode"),rs.getInt("so_luong"));
            gia_nhap_Map.put(rs.getString("barcode"),rs.getDouble("gia_nhap"));
        }
    }

    @Override
    public AnchorPane getPane() {
        return pane;
    }

    @FXML
    void cancel(ActionEvent event) {
        moneyReceivedLabel.setVisible(false);
        moneyReceivedTF.setVisible(false);
        completeBtn.setVisible(false);

        barTextField.setDisable(false);
        quanTextField.setDisable(false);

        barTextField.clear();
        quanTextField.clear();
        priceLabel.setText("");
        totalComeLabel.setText("");
    }
}
