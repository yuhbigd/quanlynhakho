package sample.Controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;
import sample.Others.ChiTietXuatHang;
import sample.Others.DialogError;
import sample.Others.View;
import sample.Others.initialization;

import java.net.URL;
import java.util.ResourceBundle;

public class addPhieuXuatForm implements Initializable {

    public StringProperty barcode;
    @FXML
    private TextField item_barcodeTF;
    @FXML
    private TextField so_luongTF;

    @FXML
    private Label gia_san_pham;


    @FXML
    void cancelAction(ActionEvent event) {
        Stage s = (Stage) so_luongTF.getScene().getWindow();
        s.close();
    }

    @FXML
    void getBarcode(ActionEvent event) {
        scanBarcodeController.barcode = new SimpleStringProperty();
        barcode = new SimpleStringProperty();
        barcode.bindBidirectional(scanBarcodeController.barcode);
        item_barcodeTF.textProperty().bindBidirectional(barcode);
        new View("/sample/Resources/FXML/scanBarcode.fxml");
    }

    @FXML
    void saveAction(ActionEvent event) {
        if (barcode != null && barcode.getValue().length() > 1) {
            item_barcodeTF.setText(barcode.getValue());
        }
        String[] bc = item_barcodeTF.getText().split(" | ");
//        if(bc.length < 2) {
//            for (Item i : initialization.allItem.values()) {
//                if (i.getBarcode().equals(bc[0])) {
//                    item_barcodeTF.setText(bc[0]+" | "+i.getItem_name());
//                    break;
//                }
//            }
//        }
        if (!initialization.allItem.containsKey(item_barcodeTF.getText())) {
            new DialogError("Sản phẩm không tồn tại");
        } else if (item_barcodeTF.getText().length() < 1 || so_luongTF.getText().length() < 1) {
            new DialogError("Không thể bỏ trống vùng nhập");
        } else if (Integer.parseInt(so_luongTF.getText()) <= 0) {
            new DialogError("Số hàng xuất ra phải lớn hơn 0");
        } else {
            int sl = initialization.allItem.get(item_barcodeTF.getText()).getSo_luong();
            if (Integer.parseInt(so_luongTF.getText()) > sl) {
                new DialogError("Không đủ số hàng để xuất");
            } else {
                gia_san_pham.setText(Double.toString(initialization.allItem.get(item_barcodeTF.getText()).getGia_ban()));
                if (addPhieuXuatController.data.containsKey(bc[0])) {
                    int so_luong = addPhieuXuatController.data.get(bc[0]).getSo_luong();
                    so_luong += Integer.parseInt(so_luongTF.getText());
                    addPhieuXuatController.data.get(bc[0]).setSo_luong(so_luong);
                } else {
                    addPhieuXuatController.data.put(bc[0], new ChiTietXuatHang(addPhieuXuatController.ma_xuat, bc[0], initialization.allItem.get(item_barcodeTF.getText()).getGia_ban(), Integer.parseInt(so_luongTF.getText())));
                }
                addPhieuXuatController.setDataForList();
                initialization.allItem.get(item_barcodeTF.getText()).setSo_luong(sl - Integer.parseInt(so_luongTF.getText()));
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TextFields.bindAutoCompletion(item_barcodeTF, initialization.allItem.keySet());
        so_luongTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    so_luongTF.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
}
