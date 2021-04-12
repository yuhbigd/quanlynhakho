package sample.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import sample.Others.View;

import java.net.URL;
import java.util.ResourceBundle;

public class assControl implements Initializable {
    @FXML
    private TextField tfield;

    @FXML
    void btnAction(ActionEvent event) {
        new View("/sample/Resources/FXML/scanBarcode.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scanBarcodeController.barcode = new SimpleStringProperty();
        tfield.textProperty().bindBidirectional(scanBarcodeController.barcode);
    }

}
