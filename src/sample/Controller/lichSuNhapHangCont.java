package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sample.Others.phieuXuat;

import java.sql.Connection;

class nhapHang{
    private String barcode;
    private String item_name;
    private Integer quantity;
    private String name;
    private String time;

    public nhapHang(String barcode, String item_name, Integer quantity, String name, String time) {
        this.barcode = barcode;
        this.item_name = item_name;
        this.quantity = quantity;
        this.name = name;
        this.time = time;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
public class lichSuNhapHangCont {

    @FXML
    private TableView<nhapHang> table;

    @FXML
    private TableColumn<nhapHang, String> barcode;

    @FXML
    private TableColumn<nhapHang, String> item_name;

    @FXML
    private TableColumn<nhapHang, Integer> quantity;

    @FXML
    private TableColumn<nhapHang, String> name;

    @FXML
    private TableColumn<nhapHang, String> time;

    private Connection con;

    private ObservableList<nhapHang> masterData = FXCollections.observableArrayList();


}
