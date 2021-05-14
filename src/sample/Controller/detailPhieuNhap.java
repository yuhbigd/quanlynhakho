package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import sample.ConnectionClass;
import sample.Others.ChiTietNhapHang;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class detailPhieuNhap implements Initializable {

    private final ObservableList<ChiTietNhapHang> masterData = FXCollections.observableArrayList();
    @FXML
    private AnchorPane pane;
    @FXML
    private TextField search_tf;
    @FXML
    private TableView<ChiTietNhapHang> listAll;
    @FXML
    private TableColumn<ChiTietNhapHang, String> ma_nhap_hang;
    @FXML
    private TableColumn<ChiTietNhapHang, String> item_barcode;
    @FXML
    private TableColumn<ChiTietNhapHang, Integer> so_luong_san_pham;
    @FXML
    private TableColumn<ChiTietNhapHang, Double> gia_san_pham;
    private Connection con;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            con = ConnectionClass.getInstances().getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ma_nhap_hang.setCellValueFactory(new PropertyValueFactory<>("ma_nhap_hang"));
        item_barcode.setCellValueFactory(new PropertyValueFactory<>("item_barcode"));
        gia_san_pham.setCellValueFactory(new PropertyValueFactory<>("gia_san_pham"));
        so_luong_san_pham.setCellValueFactory(new PropertyValueFactory<>("so_luong"));
        FilteredList<ChiTietNhapHang> FilteredList = new FilteredList<ChiTietNhapHang>(masterData, p -> true);

        search_tf.textProperty().addListener((observable, oldValue, newValue) -> {
            FilteredList.setPredicate(phieuNhap -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return phieuNhap.getItem_barcode().toLowerCase().contains(lowerCaseFilter);
            });
        });
        SortedList<ChiTietNhapHang> chiTietXuatHangSortedList = new SortedList<>(FilteredList);
        chiTietXuatHangSortedList.comparatorProperty().bind(listAll.comparatorProperty());
        listAll.setItems(chiTietXuatHangSortedList);
        setDataForList();
    }

    public void setDataForList() {
        try {
            con = ConnectionClass.getInstances().getConnection();
            String sql = "SELECT c.*,i.item_name FROM chi_tiet_lan_nhap c join item i on i.barcode = c.item_barcode  where c.ma_nhap_hang = ?;";
            PreparedStatement ptsmt = con.prepareStatement(sql);
            ptsmt.setString(1, listNhapHangController.phieunhap.getMa_nhap_hang());
            ResultSet rs = ptsmt.executeQuery();
            while (rs.next()) {
                masterData.add(new ChiTietNhapHang(rs.getString(1), rs.getString(2) + " | " + rs.getString(5), rs.getDouble(4), rs.getInt(3)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
