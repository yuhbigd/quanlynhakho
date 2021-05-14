package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import sample.ConnectionClass;
import sample.Others.ChiTietXuatHang;
import sample.Others.DialogError;
import sample.Others.View;
import sample.Others.initialization;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class addPhieuXuatController implements Initializable {

    public static Map<String, ChiTietXuatHang> data = new HashMap<>();
    public static ObservableList<ChiTietXuatHang> masterData = FXCollections.observableArrayList();
    public static ChiTietXuatHang temp;
    public static String ma_xuat;
    public static String ma_mua;
    @FXML
    private AnchorPane pane;
    @FXML
    private TextField search_tf;
    @FXML
    private TextField ma_Tf;
    @FXML
    private TableView<ChiTietXuatHang> listAll;
    @FXML
    private Button addBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private TableColumn<ChiTietXuatHang, String> ma_xuat_hang;
    @FXML
    private TableColumn<ChiTietXuatHang, String> item_barcode;
    @FXML
    private TableColumn<ChiTietXuatHang, Integer> so_luong_san_pham;
    @FXML
    private TextField ma_nguoi_mua;
    @FXML
    private TableColumn<ChiTietXuatHang, Double> gia_san_pham;
    private Connection con;

    public static void setDataForList() {
        try {
            masterData.clear();
            masterData.addAll(data.values());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addingAction(ActionEvent event) {
        new View("/sample/Resources/FXML/addPhieuXuatForm.fxml");
    }

    @FXML
    void deleteAction(ActionEvent event) {
        int sl = initialization.allItem.get(temp.getItem_barcode() + " | " + initialization.subAllItem.get(temp.getItem_barcode())).getSo_luong();
        initialization.allItem.get(temp.getItem_barcode() + " | " + initialization.subAllItem.get(temp.getItem_barcode())).setSo_luong(sl + temp.getSo_luong());
        data.remove(temp.getItem_barcode());
        setDataForList();
    }

    @FXML
    void save(ActionEvent event) {
        if (data.isEmpty()) {
            new DialogError("Bạn chưa nhập sản phẩm vào phiếu");
        } else
            try {
                String sql = "insert into xuat_hang values(?,now(),?,?);";
                PreparedStatement ptsmt = con.prepareStatement(sql);
                ptsmt.setString(1, ma_xuat);
                ptsmt.setString(2, ma_mua);
                ptsmt.setString(3, LoginController.loggerUsername);
                ptsmt.execute();
                ptsmt.close();
                String sql1 = "insert into chi_tiet_xuat_hang values (?,?,?,?);";
                ptsmt = con.prepareStatement(sql1);
                for (ChiTietXuatHang i : data.values()) {
                    ptsmt.setString(1, i.getItem_barcode());
                    ptsmt.setString(2, i.getMa_xuat_hang());
                    ptsmt.setInt(3, i.getSo_luong());
                    ptsmt.setDouble(4, i.getGia_san_pham());
                    ptsmt.execute();
                }
                ptsmt.close();
                String sql2 = "update item set so_luong = so_luong - ? where barcode = ?;";
                ptsmt = con.prepareStatement(sql2);
                for (ChiTietXuatHang i : data.values()) {
                    ptsmt.setString(2, i.getItem_barcode());
                    ptsmt.setInt(1, i.getSo_luong());
                    ptsmt.execute();
                }
                new DialogError("Thành công");
                HomeController.setChildPane("Resources/FXML/listXuatHang.fxml");
                Stage s = (Stage) addBtn.getScene().getWindow();
                s.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
    }

    @FXML
    void OKAction(ActionEvent event) {
        if (ma_Tf.getText().trim().length() < 1 || ma_nguoi_mua.getText().trim().length() < 1) {
            new DialogError("Không thể để trống mã xuất hàng");
        } else if (initialization.allPhieuXuat.containsKey(ma_Tf.getText())) {
            new DialogError("Mã xuất hàng không thể trùng");
        } else {
            String[] idK = ma_nguoi_mua.getText().split(" | ");
            for (String id : initialization.idAndName) {
                String[] idAr = id.split(" | ");
                if (idAr[0].equals(idK[0])) {
                    ma_xuat = ma_Tf.getText();
                    setDataForList();
                    listAll.setVisible(true);
                    addBtn.setVisible(true);
                    saveBtn.setVisible(true);
                    deleteBtn.setVisible(true);
                    search_tf.setVisible(true);
                    ma_mua = idAr[0];
                    return;
                }
            }
            new DialogError("Không thể tìm thấy khách hàng này");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listAll.setVisible(false);
        addBtn.setVisible(false);
        saveBtn.setVisible(false);
        deleteBtn.setVisible(false);
        search_tf.setVisible(false);
        deleteBtn.setDisable(true);
        data.clear();
        try {
            con = ConnectionClass.getInstances().getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ma_xuat_hang.setCellValueFactory(new PropertyValueFactory<>("ma_xuat_hang"));
        item_barcode.setCellValueFactory(new PropertyValueFactory<>("item_barcode"));
        gia_san_pham.setCellValueFactory(new PropertyValueFactory<>("gia_san_pham"));
        so_luong_san_pham.setCellValueFactory(new PropertyValueFactory<>("so_luong"));
        FilteredList<ChiTietXuatHang> xuatFilteredList = new FilteredList<ChiTietXuatHang>(masterData, p -> true);

        search_tf.textProperty().addListener((observable, oldValue, newValue) -> {
            xuatFilteredList.setPredicate(phieuXuat -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return phieuXuat.getItem_barcode().toLowerCase().contains(lowerCaseFilter);
            });
        });
        SortedList<ChiTietXuatHang> chiTietXuatHangSortedList = new SortedList<>(xuatFilteredList);
        chiTietXuatHangSortedList.comparatorProperty().bind(listAll.comparatorProperty());
        listAll.setItems(chiTietXuatHangSortedList);
        listAll.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 0) {
                if (listAll.getSelectionModel().getSelectedItem() != null) {
                    deleteBtn.setDisable(false);
                    temp = listAll.getSelectionModel().getSelectedItem();
                }
            }
        });
        TextFields.bindAutoCompletion(ma_Tf, initialization.allPhieuXuat.keySet());
        TextFields.bindAutoCompletion(ma_nguoi_mua, initialization.idAndName);

    }
}
