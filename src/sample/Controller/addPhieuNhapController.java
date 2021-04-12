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
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import sample.ConnectionClass;
import sample.Others.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class addPhieuNhapController implements Initializable {

    @FXML
    private TextField search_tf;

    @FXML
    private Button addBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private Button addItem;

    @FXML
    private TextField ma_Tf;

    @FXML
    private TextField ma_cong_ty;

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

    public static Map<String, ChiTietNhapHang> data = new HashMap<>();

    public static ObservableList<ChiTietNhapHang> masterData = FXCollections.observableArrayList();


    private Connection con;

    public static ChiTietNhapHang temp;

    public static String maNhap;

    public static String ma_NCC;

    @FXML
    void OKAction(ActionEvent event) {
        if(ma_Tf.getText().trim().length()<1||ma_cong_ty.getText().trim().length()<1){
            new DialogError("Không thể để trống mã xuất hàng");
        }
        else if(initialization.allPhieuNhap.keySet().contains(ma_Tf.getText())){
            new DialogError("Mã xuất hàng không thể trùng");
        }else{
            String[] idK = ma_cong_ty.getText().split(" | ");
            for(String id : initialization.idAndCongty){
                String [] idAr = id.split(" | ");
                if(idAr[0].equals(idK[0])){
                    maNhap = ma_Tf.getText();
                    setDataForList();
                    listAll.setVisible(true);
                    addBtn.setVisible(true);
                    saveBtn.setVisible(true);
                    deleteBtn.setVisible(true);
                    search_tf.setVisible(true);
                    addItem.setVisible(true);
                    ma_NCC = idAr[0];
                    return;
                }
            }
            new DialogError("Không thể tìm thấy khách hàng này");
        }
    }

    @FXML
    void addItemAction(ActionEvent event) {
        new View("/sample/Resources/FXML/insertItemForm.fxml");
    }

    @FXML
    void addingAction(ActionEvent event) {
        new View("/sample/Resources/FXML/addPhieuNhapForm.fxml");
    }

    @FXML
    void deleteAction(ActionEvent event) {
        int sl = initialization.allItem.get(temp.getItem_barcode()).getSo_luong();
        initialization.allItem.get(temp.getItem_barcode()).setSo_luong(sl - temp.getSo_luong());
        data.remove(temp.getItem_barcode());
        setDataForList();
    }

    @FXML
    void save(ActionEvent event) {
        if(data.isEmpty()) {
            new DialogError("Bạn chưa nhập sản phẩm vào phiếu");
        }
        else
            try {
                String sql = "insert into nhap_hang values(?,now(),?,?);";
                PreparedStatement ptsmt = con.prepareStatement(sql);
                ptsmt.setString(1,maNhap);
                ptsmt.setString(2,ma_NCC);
                ptsmt.setString(3,LoginController.loggerUsername);
                ptsmt.execute();
                ptsmt.close();
                String sql1 = "insert into chi_tiet_lan_nhap values (?,?,?,?);";
                ptsmt = con.prepareStatement(sql1);
                for(ChiTietNhapHang i : data.values()) {
                    ptsmt.setString(2,i.getItem_barcode());
                    ptsmt.setString(1,i.getMa_nhap_hang());
                    ptsmt.setInt(3,i.getSo_luong());
                    ptsmt.setDouble(4,i.getGia_san_pham());
                    ptsmt.execute();
                }
                ptsmt.close();
                String sql2 = "update item set so_luong = so_luong + ? where barcode = ?;";
                ptsmt = con.prepareStatement(sql2);
                for(ChiTietNhapHang i : data.values()) {
                    ptsmt.setString(2,i.getItem_barcode());
                    ptsmt.setInt(1,i.getSo_luong());
                    ptsmt.execute();
                }
                new DialogError("Thành công");
                Stage s = (Stage) addBtn.getScene().getWindow();
                s.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listAll.setVisible(false);
        addBtn.setVisible(false);
        saveBtn.setVisible(false);
        deleteBtn.setVisible(false);
        search_tf.setVisible(false);
        addItem.setVisible(false);
        deleteBtn.setDisable(true);
        ma_nhap_hang.setCellValueFactory(new PropertyValueFactory<>("ma_nhap_hang"));
        item_barcode.setCellValueFactory(new PropertyValueFactory<>("item_barcode"));
        so_luong_san_pham.setCellValueFactory(new PropertyValueFactory<>("so_luong"));
        gia_san_pham.setCellValueFactory(new PropertyValueFactory<>("gia_san_pham"));


        FilteredList<ChiTietNhapHang> nhapFilteredList = new FilteredList<ChiTietNhapHang>(masterData, p-> true);

        search_tf.textProperty().addListener((observable, oldValue, newValue) -> {
            nhapFilteredList.setPredicate(phieuNhap -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                if (phieuNhap.getItem_barcode().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<ChiTietNhapHang> chiTietNhapHangSortedList = new SortedList<>(nhapFilteredList);
        chiTietNhapHangSortedList.comparatorProperty().bind(listAll.comparatorProperty());
        listAll.setItems(chiTietNhapHangSortedList);
        listAll.setOnMouseClicked((MouseEvent event)-> {
            if(event.getClickCount() > 0) {
                if(listAll.getSelectionModel().getSelectedItem() != null) {
                    deleteBtn.setDisable(false);
                    temp = listAll.getSelectionModel().getSelectedItem();
                }
            }
        });
        TextFields.bindAutoCompletion(ma_Tf, initialization.allPhieuNhap.keySet());
        TextFields.bindAutoCompletion(ma_cong_ty,initialization.idAndCongty);
        data.clear();
        try {
            con = ConnectionClass.getInstances().getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        setDataForList();
    }
    public static void setDataForList(){
        try {
            masterData.clear();
            masterData.addAll(data.values());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
