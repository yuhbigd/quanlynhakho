package sample.Controller;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import sample.ConnectionClass;
import sample.Others.View;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardController extends AbstractController implements Initializable {
    @FXML
    private AnchorPane pane;

    @FXML
    private Label moneySpent;

    @FXML
    private Label moneyReceived;

    @FXML
    private Label diff;

    @FXML
    private Label stockOut;

    @FXML
    private LineChart<?,?> dayChart;

    @FXML
    private PieChart circleChart;

    @FXML
    private Button itemSoldBtn;

    @FXML
    private CategoryAxis xAxis;


    @FXML
    private NumberAxis yAxis;

    @FXML
    private Label date;


    @Override
    public AnchorPane getPane() {
        return pane;
    }


    private Connection con;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        LocalDateTime now = LocalDateTime.now();

        date.setText(dtf.format(now));

            try {
                con = ConnectionClass.getInstances().getConnection();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        try {
            dayChart.getData().addAll(dataOfLineChart());
            dayChart.setCreateSymbols(false);
            createCircleChart();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        diff.setText("0");
        try {
            stockOut.setText(getCountOutOfStock());
            moneySpent.setText(getMoneySpent());
            moneyReceived.setText(getMoneyReceived());
            diff.setText(Double.toString(Double.parseDouble(moneyReceived.getText()) - Double.parseDouble(moneySpent.getText())));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private String getCountOutOfStock() throws SQLException {
        String sql = "select count(*) from item where so_luong = 0 and trang_thai = 'dang ban';";
        PreparedStatement ptsmt = con.prepareStatement(sql);
        String count = "0";
        ResultSet rs = ptsmt.executeQuery();
        if(rs.next()) {
            count = rs.getString(1);
        }
        if(count!=null) {
            return count;
        }
        return "0";
    }

    private String getMoneyReceived() throws SQLException {
        String sql = "select sum(tien_nhan) from ban_hang where date(ngay_ban) = date(localtime())";
        PreparedStatement ptsmt = con.prepareStatement(sql);
        String count = "0";
        ResultSet rs = ptsmt.executeQuery();
        if(rs.next()) {
            count = rs.getString(1);
        }
        if(count!=null) {
            return count;
        }
        return "0";
    }


    private String getMoneySpent() throws SQLException {
        String sql = "select sum(tien_tra_lai) from ban_hang where date(ngay_ban) = date(localtime())";
        PreparedStatement ptsmt = con.prepareStatement(sql);
        String count = "0";
        ResultSet rs = ptsmt.executeQuery();
        if(rs.next()) {
            count = rs.getString(1);
        }
        if(count!=null) {
            return count;
        }
        return "0";
    }

    private XYChart.Series dataOfLineChart() throws SQLException  {
        XYChart.Series series = new XYChart.Series();
        series.setName("số tiền lời");
        String sql = "SELECT sum(tien_nhan-tien_tra_lai)-(gia_nhap_vao * so_luong) as \"ss\",date(ngay_ban)\n" +
                "from ban_hang\n" +
                "where date(ngay_ban) between date_add(now(),interval -1 month) and date(now())\n" +
                "group by date(ngay_ban);";
        PreparedStatement ptsmt = con.prepareStatement(sql);

        ResultSet rs = ptsmt.executeQuery();
        DateFormat df = new SimpleDateFormat("dd/MM");

        while(rs.next()) {
            series.getData().add(new XYChart.Data(df.format(rs.getDate(2)),rs.getDouble(1)));
        }
        return series;
    }

    private void createCircleChart() throws SQLException {
        String sql = "SELECT it.ten,sum(b.so_luong)\n" +
                "from ban_hang b join item i on b.Item_barcode = i.barcode\n" +
                "join item_type it on i.Item_type_id = it.id \n" +
                "where date(b.ngay_ban) between date_add(date(now()),interval -1 month) and date(now())\n" +
                "group by it.id;";
        PreparedStatement ptsmt = con.prepareStatement(sql);

        ResultSet rs = ptsmt.executeQuery();


        ObservableList<PieChart.Data> cChartData = FXCollections.observableArrayList();
        while (rs.next()) {
            cChartData.add(new PieChart.Data(rs.getString(1),rs.getDouble(2)));
        }
        circleChart.setData(cChartData);
        circleChart.setStartAngle(90);

    }

    public void itemSoldAction(ActionEvent event) {
        new View("/sample/Resources/FXML/todaySell.fxml");
    }
}
