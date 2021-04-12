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
import sample.Others.View;
import sample.Others.initialization;
import sample.Others.phieuNhap;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class listNhapHangController extends AbstractController implements Initializable {

    @FXML
    private AnchorPane pane;

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

    private Connection con;

    private ObservableList<phieuNhap> masterData = FXCollections.observableArrayList();

    public static phieuNhap phieunhap;

    @FXML
    private TextField search_tf;

    @FXML
    private Button addBtn;

    @FXML
    private Button detailBtn;

    @FXML
    void addItem(ActionEvent event) {
        new View("/sample/Resources/FXML/insertItemForm.fxml");

    }

    @FXML
    void addingAction(ActionEvent event) {
        new View("/sample/Resources/FXML/addPhieuNhap.fxml");
    }

    @FXML
    void detailAction(ActionEvent event) {
        new View("/sample/Resources/FXML/detailPhieuNhap.fxml");
        detailBtn.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        detailBtn.setDisable(true);
        ma_nhap_hang.setCellValueFactory(new PropertyValueFactory<>("ma_nhap_hang"));
        ma_cong_ty.setCellValueFactory(new PropertyValueFactory<>("ma_cong_ty"));
        thoi_gian_nhap.setCellValueFactory(new PropertyValueFactory<>("thoi_gian_nhap"));
        tai_khoan_nhap.setCellValueFactory(new PropertyValueFactory<>("nguoi_nhap"));
        tong_tien_da_tra.setCellValueFactory(new PropertyValueFactory<>("tong_tien"));
        FilteredList<phieuNhap> nhapFilteredList = new FilteredList<phieuNhap>(masterData, p-> true);
        search_tf.textProperty().addListener((observable, oldValue, newValue) -> {
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
                    detailBtn.setDisable(false);
                    phieunhap = listItem.getSelectionModel().getSelectedItem();
                    if(event.getClickCount() > 1) {
                        new View("/sample/Resources/FXML/detailPhieuNhap.fxml");
                        detailBtn.setDisable(true);
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

    public void setDataForList(){
        masterData.clear();
        initialization.setDataForPhieuNhap();
        masterData.addAll(initialization.allPhieuNhap.values());
    }

    @Override
    public AnchorPane getPane() {
        return pane;
    }
}
