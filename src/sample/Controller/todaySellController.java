package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.ConnectionClass;
import sample.Others.SellItem;

import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class todaySellController implements Initializable {

    @FXML
    private TableView<SellItem> table;

    @FXML
    private TableColumn<SellItem, String> barcode;

    @FXML
    private TableColumn<SellItem, String> item_name;

    @FXML
    private TableColumn<SellItem,String> user_name;

    @FXML
    private TableColumn<SellItem, Double> quantity;

    @FXML
    private TableColumn<SellItem, Double> price;

    @FXML
    private TableColumn<SellItem, Double> received;

    @FXML
    private TableColumn<SellItem, String> timeStamp;

    @FXML
    private TableColumn<SellItem, Double> spent;
    @FXML
    private TableColumn<SellItem, Double> gia_nhapCL;

    ObservableList<SellItem> list;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            createSellItem();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        item_name.setCellValueFactory(new PropertyValueFactory<>("item_name"));
        user_name.setCellValueFactory(new PropertyValueFactory<>("user_name"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        received.setCellValueFactory(new PropertyValueFactory<>("moneyReceived"));
        spent.setCellValueFactory(new PropertyValueFactory<>("moneySpent"));
        timeStamp.setCellValueFactory(new PropertyValueFactory<>("time_stamp"));
        gia_nhapCL.setCellValueFactory(new PropertyValueFactory<>("gia_nhap"));


        table.setItems(list);

    }


    private void createSellItem() throws SQLException {
        list = FXCollections.observableArrayList();
        String sql = "SELECT b.Item_barcode,b.Login_user_name,i.item_name,b.so_luong,b.gia_ban,b.ngay_ban,b.tien_nhan,b.tien_tra_lai,b.gia_nhap_vao\n" +
                " from ban_hang b join item i on b.Item_barcode = i.barcode;";
        Connection con = null;

        try {
            con = ConnectionClass.getInstances().getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Date date = null;
        PreparedStatement ptsmt = con.prepareStatement(sql);
        ResultSet rs = ptsmt.executeQuery();
        while(rs.next()) {
            Timestamp timestamp = rs.getTimestamp(6);
            if (timestamp != null)
                date = new java.util.Date(timestamp.getTime());
            DateFormat df = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
            list.add(new SellItem(rs.getString(2),rs.getString(1),rs.getString(3),rs.getDouble(5),rs.getDouble(4),rs.getDouble(7),rs.getDouble(8),df.format(date).toString(),rs.getDouble(9)));
        }
    }
}
