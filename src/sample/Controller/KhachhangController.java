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
import sample.ConnectionClass;
import sample.Others.Khachhang;
import sample.Others.View;
import sample.Others.initialization;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class KhachhangController extends AbstractController implements Initializable {

    @FXML
    private AnchorPane pane;

    @FXML
    private TableView<Khachhang> listItem;

    @FXML
    private TableColumn<Khachhang, String> ma_khach_hang;

    @FXML
    private TableColumn<Khachhang, String> ten_khach_hang;

    @FXML
    private TableColumn<Khachhang, String> dia_chi;

    @FXML
    private TableColumn<Khachhang, String> so_dien_thoai;

    @FXML
    private TableColumn<Khachhang, Integer> so_lan_mua;

    @FXML
    private TableColumn<Khachhang, Double> tong_tien_da_tra;

    @FXML
    private TextField search_tf;

    @FXML
    private Button addBtn;

    @FXML
    private Button updateBtn;

    private Connection con;

    public static Khachhang khachhang;

    private ObservableList<Khachhang> masterData = FXCollections.observableArrayList();
    @FXML
    void addingAction(ActionEvent event) {
        new View("/sample/Resources/FXML/insertKhachHangForm.fxml");
    }

    @FXML
    void updatingAction(ActionEvent event) {
        new View("/sample/Resources/FXML/updateKhachHang.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateBtn.setDisable(true);
        try {
            con = ConnectionClass.getInstances().getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ma_khach_hang.setCellValueFactory(new PropertyValueFactory<>("ma_khach"));
        ten_khach_hang.setCellValueFactory(new PropertyValueFactory<>("ten_khach_hang"));
        dia_chi.setCellValueFactory(new PropertyValueFactory<>("dia_chi"));
        so_dien_thoai.setCellValueFactory(new PropertyValueFactory<>("so_dien_thoai"));
        so_lan_mua.setCellValueFactory(new PropertyValueFactory<>("so_lan_mua"));
        tong_tien_da_tra.setCellValueFactory(new PropertyValueFactory<>("tong_tien_da_tra"));
        setDataForList();
        FilteredList<Khachhang> itemFilteredList = new FilteredList<>(masterData, p -> true);
        search_tf.textProperty().addListener((observable, oldValue, newValue) -> {
            itemFilteredList.setPredicate(khach_hang -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                if (khach_hang.getMa_khach().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (khach_hang.getTen_khach_hang().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<Khachhang> sortedKhachhang =  new SortedList<>(itemFilteredList);
        sortedKhachhang.comparatorProperty().bind(listItem.comparatorProperty());
        listItem.setItems(sortedKhachhang);
        listItem.setOnMouseClicked((MouseEvent event)-> {
            if(event.getClickCount() > 0) {
                if(listItem.getSelectionModel().getSelectedItem() != null) {
                    updateBtn.setDisable(false);
                    khachhang = listItem.getSelectionModel().getSelectedItem();
                    if(event.getClickCount() > 1) {
                        new View("/sample/Resources/FXML/updateKhachHang.fxml");
                    }
                }
            }
        });
    }
    public void setDataForList() {
        masterData.clear();
        initialization.setDataForKhachHang();
        masterData.addAll(initialization.allKhachHang.values());

    }

    @Override
    public AnchorPane getPane() {
        return pane;
    }
}
