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
import javafx.scene.input.MouseEvent;
import sample.ConnectionClass;
import sample.Others.View;
import sample.Others.phieuNhap;

import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class listNhapHangToday implements Initializable {

    @FXML
    private TableView<phieuNhap> listItem;

    @FXML
    private TableColumn<phieuNhap, String> ma_nhap_hang;

    @FXML
    private TableColumn<phieuNhap, String> ma_cong_ty;

    @FXML
    private TableColumn<phieuNhap, String> thoi_gian_nhap;

    @FXML
    private TableColumn<phieuNhap, String> tai_khoan_nhap;

    @FXML
    private TableColumn<phieuNhap, Double> tong_tien_da_tra;

    @FXML
    private TextField searchTF;

    private Connection con;

    private ObservableList<phieuNhap> masterData = FXCollections.observableArrayList();

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
        ma_cong_ty.setCellValueFactory(new PropertyValueFactory<>("ma_cong_ty"));
        thoi_gian_nhap.setCellValueFactory(new PropertyValueFactory<>("thoi_gian_nhap"));
        tai_khoan_nhap.setCellValueFactory(new PropertyValueFactory<>("nguoi_nhap"));
        tong_tien_da_tra.setCellValueFactory(new PropertyValueFactory<>("tong_tien"));
        FilteredList<phieuNhap> nhapFilteredList = new FilteredList<phieuNhap>(masterData, p-> true);
        searchTF.textProperty().addListener((observable, oldValue, newValue) -> {
            nhapFilteredList.setPredicate(phieuNhap -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                if (phieuNhap.getMa_cong_ty().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (phieuNhap.getMa_nhap_hang().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (phieuNhap.getNguoi_nhap().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<phieuNhap> nhapSortedList = new SortedList<>(nhapFilteredList);
        nhapSortedList.comparatorProperty().bind(listItem.comparatorProperty());
        listItem.setItems(nhapSortedList);
        listItem.setOnMouseClicked((MouseEvent event)-> {
            if(event.getClickCount() > 0) {
                if(listItem.getSelectionModel().getSelectedItem() != null) {
                    listNhapHangController.phieunhap = listItem.getSelectionModel().getSelectedItem();
                    if(event.getClickCount() > 1) {
                        new View("/sample/Resources/FXML/detailPhieuNhap.fxml");
                    }
                }
            }
        });
        setData();
    }

    public void setData(){
        try {
            String sql = "select x.*,sum(c.gia_san_pham*c.so_luong) as \"tong gia\" from chi_tiet_lan_nhap as c join nhap_hang as x on c.ma_nhap_hang = x.ma_nhap_hang where date(x.thoi_gian_nhap) = date(localtime) group by x.ma_nhap_hang;";
            PreparedStatement ptsmt = con.prepareStatement(sql);
            ResultSet rs = ptsmt.executeQuery();
            Date date = null;
            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp(2);
                if (timestamp != null)
                    date = new java.util.Date(timestamp.getTime());
                DateFormat df = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
               masterData.add(new phieuNhap(rs.getString(1),df.format(date),rs.getString(3),rs.getString(4),rs.getDouble(5)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
