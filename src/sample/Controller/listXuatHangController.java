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
import sample.Others.phieuXuat;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class listXuatHangController extends AbstractController implements Initializable {

    @FXML
    private AnchorPane pane;

    @FXML
    private TableView<phieuXuat> listItem;

    @FXML
    private TableColumn<phieuXuat,String> ma_khach_hang;

    @FXML
    private TableColumn<phieuXuat,String> ma_xuat_hang;

    @FXML
    private TableColumn<phieuXuat,String> thoi_gian_xuat;

    @FXML
    private TableColumn<phieuXuat,String> tai_khoan_ban;

    @FXML
    private TableColumn<phieuXuat,Double> tong_tien_da_tra;

    @FXML
    private TextField search_tf;

    @FXML
    private Button addBtn;

    @FXML
    private Button detailBtn;

    private Connection con;

    private ObservableList<phieuXuat> masterData = FXCollections.observableArrayList();

    public static phieuXuat phieuXuat;

    @FXML
    void addingAction(ActionEvent event) {
        new View("/sample/Resources/FXML/addPhieuXuat.fxml");
    }

    @FXML
    void detailAction(ActionEvent event) {
        new View("/sample/Resources/FXML/detailPhieuXuat.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        detailBtn.setDisable(true);
        ma_khach_hang.setCellValueFactory(new PropertyValueFactory<>("ma_khach_hang"));
        ma_xuat_hang.setCellValueFactory(new PropertyValueFactory<>("ma_xuat_hang"));
        thoi_gian_xuat.setCellValueFactory(new PropertyValueFactory<>("thoi_gian_xuat"));
        tai_khoan_ban.setCellValueFactory(new PropertyValueFactory<>("nguoi_ban"));
        tong_tien_da_tra.setCellValueFactory(new PropertyValueFactory<>("tong_tien"));
        FilteredList<phieuXuat> xuatFilteredList = new FilteredList<phieuXuat>(masterData,p-> true);
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
                if (phieuXuat.getNguoi_ban().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<phieuXuat> xuatSortedList = new SortedList<>(xuatFilteredList);
        xuatSortedList.comparatorProperty().bind(listItem.comparatorProperty());
        listItem.setItems(xuatSortedList);
        listItem.setOnMouseClicked((MouseEvent event)-> {
            if(event.getClickCount() > 0) {
                if(listItem.getSelectionModel().getSelectedItem() != null) {
                    detailBtn.setDisable(false);
                    phieuXuat = listItem.getSelectionModel().getSelectedItem();
                    if(event.getClickCount() > 1) {
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

    public void setDataForList(){
        masterData.clear();
        initialization.setDataForPhieuXuat();
        masterData.addAll(initialization.allPhieuXuat.values());
    }

    @Override
    public AnchorPane getPane() {
        return pane;
    }
}
