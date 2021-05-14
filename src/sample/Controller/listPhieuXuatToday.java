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
import sample.Others.phieuXuat;

import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class listPhieuXuatToday implements Initializable {
    private final ObservableList<phieuXuat> masterData = FXCollections.observableArrayList();
    @FXML
    private TableView<phieuXuat> listItem;
    @FXML
    private TableColumn<phieuXuat, String> ma_khach_hang;
    @FXML
    private TableColumn<phieuXuat, String> ma_xuat_hang;
    @FXML
    private TableColumn<phieuXuat, String> thoi_gian_xuat;
    @FXML
    private TableColumn<phieuXuat, String> tai_khoan_ban;
    @FXML
    private TableColumn<phieuXuat, Double> tong_tien_da_tra;
    @FXML
    private TextField search_tf;
    private Connection con;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ma_khach_hang.setCellValueFactory(new PropertyValueFactory<>("ma_khach_hang"));
        ma_xuat_hang.setCellValueFactory(new PropertyValueFactory<>("ma_xuat_hang"));
        thoi_gian_xuat.setCellValueFactory(new PropertyValueFactory<>("thoi_gian_xuat"));
        tai_khoan_ban.setCellValueFactory(new PropertyValueFactory<>("nguoi_ban"));
        tong_tien_da_tra.setCellValueFactory(new PropertyValueFactory<>("tong_tien"));
        FilteredList<phieuXuat> xuatFilteredList = new FilteredList<phieuXuat>(masterData, p -> true);
        search_tf.textProperty().addListener((observable, oldValue, newValue) -> {
            xuatFilteredList.setPredicate(phieuXuat -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                if (phieuXuat.getMa_khach_hang().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (phieuXuat.getMa_xuat_hang().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return phieuXuat.getNguoi_ban().toLowerCase().contains(lowerCaseFilter);
            });
        });
        SortedList<phieuXuat> xuatSortedList = new SortedList<>(xuatFilteredList);
        xuatSortedList.comparatorProperty().bind(listItem.comparatorProperty());
        listItem.setItems(xuatSortedList);
        listItem.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 0) {
                if (listItem.getSelectionModel().getSelectedItem() != null) {
                    listXuatHangController.phieuXuat = listItem.getSelectionModel().getSelectedItem();
                    if (event.getClickCount() > 1) {
                        new View("/sample/Resources/FXML/detailPhieuXuat.fxml");
                    }
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

        setDataForList();

    }

    public void setDataForList() {
        try {
            String sql = "select x.*,sum(c.gia_san_pham*c.so_luong_san_pham) from chi_tiet_xuat_hang as c join xuat_hang as x on c.ma_xuat_hang = x.ma_xuat_hang where date(x.thoi_gian_xuat) = date(localtime) group by x.ma_xuat_hang;";
            PreparedStatement ptsmt = con.prepareStatement(sql);
            ResultSet rs = ptsmt.executeQuery();
            Date date = null;
            while (rs.next()) {
                Timestamp timestamp = rs.getTimestamp(2);
                if (timestamp != null)
                    date = new java.util.Date(timestamp.getTime());
                DateFormat df = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss");
                masterData.add(new phieuXuat(rs.getString(1), df.format(date), rs.getString(3), rs.getString(4), rs.getDouble(5)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
