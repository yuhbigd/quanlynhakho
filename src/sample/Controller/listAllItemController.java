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
import sample.Others.Item;
import sample.Others.View;
import sample.Others.initialization;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class listAllItemController extends AbstractController implements Initializable  {
    @FXML
    private AnchorPane pane;

    @FXML
    private TableView<Item> listItem;

    @FXML
    private TableColumn<Item, String> barcode;

    @FXML
    private TableColumn<Item, String> item_name;

    @FXML
    private TableColumn<Item, Integer> quantity;

    @FXML
    private TableColumn<Item, Double> price;

    @FXML
    private TableColumn<Item, String> type;

    @FXML
    private TableColumn<Item, String> status;
    @FXML
    private TableColumn<Item, Double> gia_nhapCL;

    @FXML
    private TextField search_tf;

    @FXML
    private Button searchBtn;

    @FXML
    private Button addBtn;

    @FXML
    private Button updateBtn;

    public static Item item;

    private Connection con = null;

    public static ArrayList<String> item_Barcode = new ArrayList<>();

    public static ArrayList<String> item_Type = new ArrayList<>();


    private ObservableList<Item> masterData = FXCollections.observableArrayList();



    public listAllItemController() throws SQLException, ClassNotFoundException {
        setDataForList();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateBtn.setDisable(true);
        barcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        item_name.setCellValueFactory(new PropertyValueFactory<>("item_name"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("so_luong"));
        price.setCellValueFactory(new PropertyValueFactory<>("gia_ban"));
        type.setCellValueFactory(new PropertyValueFactory<>("item_type"));
        status.setCellValueFactory(new PropertyValueFactory<>("trang_thai"));
        gia_nhapCL.setCellValueFactory(new PropertyValueFactory<>("gia_nhap"));
        FilteredList<Item> itemFilteredList = new FilteredList<>(masterData, p -> true);

        search_tf.textProperty().addListener((observable, oldValue, newValue) -> {
            itemFilteredList.setPredicate(item -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                if (item.getBarcode().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (item.getItem_name().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Item> sortedItem = new SortedList<>(itemFilteredList);

        sortedItem.comparatorProperty().bind(listItem.comparatorProperty());

        listItem.setItems(sortedItem);


        listItem.setOnMouseClicked((MouseEvent event)-> {
            if(event.getClickCount() > 0) {
                if(listItem.getSelectionModel().getSelectedItem() != null) {
                    updateBtn.setDisable(false);
                    item = listItem.getSelectionModel().getSelectedItem();
                    if(event.getClickCount() > 1) {
                        new View("/sample/Resources/FXML/lichSuNhapHang.fxml");
                    }
                }
            }
        });
    }

    public void setDataForList() throws SQLException, ClassNotFoundException {
        item_Barcode.clear();
        item_Type.clear();
        masterData.clear();
        initialization.setDataForItem();
        masterData.addAll(initialization.allItem.values());
    }


    @FXML
    void addingAction(ActionEvent event) {
        new View("/sample/Resources/FXML/insertItemForm.fxml");
    }

    @FXML
    void searchAction(ActionEvent event) {

    }

    @FXML
    void updatingAction(ActionEvent event) {
        new View("/sample/Resources/FXML/updateItemForm.fxml");

    }

    @Override
    public AnchorPane getPane() {
        return pane;
    }

}
