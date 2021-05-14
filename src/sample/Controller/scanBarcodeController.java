package sample.Controller;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

public class scanBarcodeController implements Initializable {
    public static StringProperty barcode;
    private final ObjectProperty<Image> imageProperty = new SimpleObjectProperty<Image>();
    @FXML
    private ImageView webcamPane;
    @FXML
    private AnchorPane pane;
    private BufferedImage grabbedImage;
    private Webcam webcam = null;
    private Thread th;
    private boolean stopCamera;
    @FXML
    private Label name;
    @FXML
    private Button close;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stopCamera = false;
        webcam = Webcam.getDefault();
        webcam.open();
        webcamPane.imageProperty().bind(imageProperty);
        startWebCam();
    }

    public void startWebCam() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (!stopCamera) {
                    try {
                        if ((grabbedImage = webcam.getImage()) != null) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    final Image mainImage = SwingFXUtils.toFXImage(grabbedImage, null);
                                    imageProperty.set(mainImage);

                                    try {
                                        LuminanceSource source = new BufferedImageLuminanceSource(grabbedImage);
                                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                                        Result result = new MultiFormatReader().decode(bitmap);
                                        if (result.getText() != null) {
                                            barcode.setValue(result.getText());
                                            stopCamera = true;
                                            try {
                                                th.interrupt();
                                                Thread.sleep(5);
                                                webcam.close();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    } catch (NotFoundException e) {
                                        //pass
                                    }
                                    if (stopCamera == true) {
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                name.setText(barcode.getValue());
                                            }
                                        });
                                        return;
                                    }
                                }
                            });
                            grabbedImage.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };
        th = new Thread(task);
        th.setDaemon(true);
        th.start();

    }

    @FXML
    public void closeAc(ActionEvent event) {
        if (th.isAlive()) {
            try {
                th.interrupt();
                webcam.close();
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Stage s = (Stage) close.getScene().getWindow();
        s.close();
    }

}
