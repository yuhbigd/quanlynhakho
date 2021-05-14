package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.ConnectionClass;
import sample.Others.lichSuNhapHang;

import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;


public class lichSuNhapHangCont implements Initializable {

    @FXML
    private TableView<lichSuNhapHang> table;

    @FXML
    private TableColumn<lichSuNhapHang, String> barcode;

    @FXML
    private TableColumn<lichSuNhapHang, String> item_name;

    @FXML
    private TableColumn<lichSuNhapHang, Integer> quantity;

    @FXML
    private TableColumn<lichSuNhapHang, String> name;

    @FXML
    private TableColumn<lichSuNhapHang, String> time;

    @FXML
    private TableColumn<lichSuNhapHang, String> maDonNhap;

    private Connection con;

    private ObservableList<lichSuNhapHang> masterData = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        item_name.setCellValueFactory(new PropertyValueFactory<>("item_name"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        time.setCellValueFactory(new PropertyValueFactory<>("time"));
        maDonNhap.setCellValueFactory(new PropertyValueFactory<>("maNhapHang"));
        table.setItems(masterData);
        setData();
    }
    public void setData(){
        try {
            con = ConnectionClass.getInstances().getConnection();
            String sql = "select c.ma_nhap_hang,i.barcode,i.item_name,ct.so_luong,c.thoi_gian_nhap,c.login_user_name from nhap_hang c join chi_tiet_lan_nhap ct on ct.ma_nhap_hang = c.ma_nhap_hang join item i on ct.item_barcode = i.barcode where ct.item_barcode = ?;";
            PreparedStatement ptsmt = con.prepareStatement(sql);
            ptsmt.setString(1,listAllItemController.item.getBarcode());
            ResultSet rs = ptsmt.executeQuery();
            Date date = null;
            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp(5);
                if (timestamp != null)
                    date = new java.util.Date(timestamp.getTime());
                DateFormat df = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
                masterData.add(new lichSuNhapHang(rs.getString(2),rs.getString(1),rs.getString(3),rs.getInt(4),rs.getString(6),df.format(date)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e){

        }
    }
}
