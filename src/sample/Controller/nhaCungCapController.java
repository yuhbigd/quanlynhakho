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
import sample.Others.Congty;
import sample.Others.View;
import sample.Others.initialization;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class nhaCungCapController extends AbstractController implements Initializable {

    @FXML
    private AnchorPane pane;

    @FXML
    private TableView<Congty> listItem;

    @FXML
    private TableColumn<Congty, String> ma_cong_ty;

    @FXML
    private TableColumn<Congty, String> ten_cong_ty;

    @FXML
    private TableColumn<Congty, String> dia_chi;

    @FXML
    private TableColumn<Congty,String> so_dien_thoai;

    @FXML
    private TableColumn<Congty,Integer> so_lan_nhap;

    @FXML
    private TableColumn<Congty,Double> tong_tien_da_tra;

    @FXML
    private TextField search_tf;

    @FXML
    private Button addBtn;

    @FXML
    private Button updateBtn;
    private Connection con;

    public static Congty congty;

    private ObservableList<Congty> masterData = FXCollections.observableArrayList();


    @FXML
    void addingAction(ActionEvent event) {
        new View("/sample/Resources/FXML/insertNCC.fxml");
    }

    @FXML
    void updatingAction(ActionEvent event) {
        new View("/sample/Resources/FXML/updateNCC.fxml");
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
        ma_cong_ty.setCellValueFactory(new PropertyValueFactory<>("ma_cong_ty"));
        ten_cong_ty.setCellValueFactory(new PropertyValueFactory<>("ten_cong_ty"));
        dia_chi.setCellValueFactory(new PropertyValueFactory<>("dia_chi"));
        so_dien_thoai.setCellValueFactory(new PropertyValueFactory<>("so_dien_thoai"));
        so_lan_nhap.setCellValueFactory(new PropertyValueFactory<>("so_lan_nhap"));
        tong_tien_da_tra.setCellValueFactory(new PropertyValueFactory<>("tong_tien_da_tra"));
        setDataForList();
        FilteredList<Congty> itemFilteredList = new FilteredList<>(masterData, p -> true);
        search_tf.textProperty().addListener((observable, oldValue, newValue) -> {
            itemFilteredList.setPredicate(cong_ty -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                if (cong_ty.getMa_cong_ty().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (cong_ty.getTen_cong_ty().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<Congty> sortedCongty =  new SortedList<>(itemFilteredList);
        sortedCongty.comparatorProperty().bind(listItem.comparatorProperty());
        listItem.setItems(sortedCongty);
        listItem.setOnMouseClicked((MouseEvent event)-> {
            if(event.getClickCount() > 0) {
                if(listItem.getSelectionModel().getSelectedItem() != null) {
                    updateBtn.setDisable(false);
                    congty = listItem.getSelectionModel().getSelectedItem();
                    if(event.getClickCount() > 1) {
                        new View("/sample/Resources/FXML/updateNCC.fxml");
                    }
                }
            }
        });
    }
    public void setDataForList(){
        masterData.clear();
        initialization.setDataForCongTy();
        masterData.addAll(initialization.allCongty.values());
    }

    @Override
    public AnchorPane getPane() {
        return pane;
    }
}
