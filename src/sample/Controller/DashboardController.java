package sample.Controller;

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
    private Button litsNhap;

    @FXML
    private LineChart<?, ?> dayChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private Label soLanXuat;

    @FXML
    private Label soLanNhap;

    @FXML
    private PieChart circleChart;

    @FXML
    private Label date;

    @FXML
    private Button listXuat;

    @FXML
    private Label tongNhap;

    @FXML
    private Label tongXuat;

    @FXML
    private Label outOfStock;

    private Connection con;

    @FXML
    public void listNhap(ActionEvent event){
        new View("/sample/Resources/FXML/listNhapHangToday.fxml");
    }
    @FXML
    public void listXuat(ActionEvent event){
        new View("/sample/Resources/FXML/listPhieuXuatToday.fxml");
    }

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
            counting();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            dayChart.getData().addAll(dataOfLineChart());
            dayChart.setCreateSymbols(false);
            createCircleChart();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public AnchorPane getPane() {
        return pane;
    }

    public XYChart.Series dataOfLineChart() throws SQLException  {
        XYChart.Series series = new XYChart.Series();
        series.setName("số tiền lời từ xuất hàng");
        String sql = "select sum(c.so_luong_san_pham * c.gia_san_pham) - sum(c.so_luong_san_pham * i.gia_nhap ), date(x.thoi_gian_xuat)  from xuat_hang as x join chi_tiet_xuat_hang as c on x.ma_xuat_hang = c.ma_xuat_hang join item i on i.barcode = c.item_barcode where date(x.thoi_gian_xuat) between date_add(now(),interval -1 month) and date(now()) group by date(x.thoi_gian_xuat);";
        PreparedStatement ptsmt = con.prepareStatement(sql);

        ResultSet rs = ptsmt.executeQuery();
        DateFormat df = new SimpleDateFormat("dd/MM");

        while(rs.next()) {
            series.getData().add(new XYChart.Data(df.format(rs.getDate(2)),rs.getDouble(1)));
        }
        return series;
    }


    public void createCircleChart() throws SQLException {
        String sql = "select i.item_name, sum(c.so_luong_san_pham) as \"diff\" from xuat_hang as x join chi_tiet_xuat_hang as c on x.ma_xuat_hang = c.ma_xuat_hang join item i on i.barcode = c.item_barcode where date(x.thoi_gian_xuat) between date_add(now(),interval -1 month) and date(now())group by i.item_name;";
        PreparedStatement ptsmt = con.prepareStatement(sql);

        ResultSet rs = ptsmt.executeQuery();


        ObservableList<PieChart.Data> cChartData = FXCollections.observableArrayList();
        while (rs.next()) {
            cChartData.add(new PieChart.Data(rs.getString(1),rs.getDouble(2)));
        }
        circleChart.setData(cChartData);
        circleChart.setStartAngle(90);

    }

    public void counting() throws SQLException {
        String sql = "select count(*) from item where so_luong = 0 and trang_thai = 'đang bán';";
        PreparedStatement ptsmt = con.prepareStatement(sql);
        String count = "0";
        ResultSet rs = ptsmt.executeQuery();
        while(rs.next()) {
            count = rs.getString(1);
        }
        outOfStock.setText(count);
        ptsmt.close();

        String sql1 = "select count(*) from xuat_hang where date(thoi_gian_xuat) = date(localtime());";
        ptsmt = con.prepareStatement(sql1);
        rs = ptsmt.executeQuery();
        while(rs.next()) {
            soLanXuat.setText(rs.getString(1));
        }
        ptsmt.close();
        String sql2 = "select count(*) from nhap_hang where date(thoi_gian_nhap) = date(localtime());";
        ptsmt = con.prepareStatement(sql2);
        rs = ptsmt.executeQuery();
        while(rs.next()) {
            soLanNhap.setText(rs.getString(1));
        }

        String sql3 = "select if(sum(c.so_luong_san_pham*c.gia_san_pham) is null,0,sum(c.so_luong_san_pham*c.gia_san_pham)) from xuat_hang x join chi_tiet_xuat_hang c on c.ma_xuat_hang = x.ma_xuat_hang where date(thoi_gian_xuat) = date(localtime);";
        ptsmt = con.prepareStatement(sql3);
        rs = ptsmt.executeQuery();
        while(rs.next()) {
            tongXuat.setText(rs.getString(1));
        }
        String sql4 = "select if(sum(c.so_luong*c.gia_san_pham) is null,0,sum(c.so_luong*c.gia_san_pham)) from nhap_hang x join chi_tiet_lan_nhap c on c.ma_nhap_hang = x.ma_nhap_hang where date(thoi_gian_nhap) = date(localtime);";
        ptsmt = con.prepareStatement(sql4);
        rs = ptsmt.executeQuery();
        while(rs.next()) {
            tongNhap.setText(rs.getString(1));
        }



    }
}
