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
import sample.Others.DialogError;
import sample.Others.Employee;
import sample.Others.View;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class listEmpController extends AbstractController implements Initializable {

    @FXML
    private AnchorPane pane;

    @FXML
    private TableView<Employee> listEmp;

    @FXML
    private TableColumn<Employee, String> userNameCol;

    @FXML
    private TableColumn<Employee, String> passCol;

    @FXML
    private TableColumn<Employee, String> posCol;

    @FXML
    private TableColumn<Employee, String> sexCol;

    @FXML
    private TableColumn<Employee, String> pNumCol;

    @FXML
    private TableColumn<Employee, String> lCol;

    @FXML
    private TableColumn<Employee, String> nameCol;

    @FXML
    private TextField search_tf;

    @FXML
    private Button searchBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button updateBtn;
    public static Employee emp;
    private Connection con;
    private ObservableList<Employee> masterData = FXCollections.observableArrayList();

    @FXML
    void addingAction(ActionEvent event) {
        new View("/sample/Resources/FXML/insertEmpForm.fxml");
    }

    @FXML
    void searchAction(ActionEvent event) {

    }

    @FXML
    void updatingAction(ActionEvent event) {
        new View("/sample/Resources/FXML/updateEmpForm.fxml");
        updateBtn.setDisable(true);
    }
    @FXML
    void deleteAction(ActionEvent event) {
        try {
            Connection con = ConnectionClass.getInstances().getConnection();
            String sql ="delete from login where user_name = ?";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1,emp.getUser_name());
            pstmt.executeUpdate();
            new DialogError("Thành Công");
            deleteBtn.setDisable(true);
            setDataForList();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public listEmpController() {
        setDataForList();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateBtn.setDisable(true);
        deleteBtn.setDisable(true);
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("user_name"));
        passCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        posCol.setCellValueFactory(new PropertyValueFactory<>("ten_chuc_vu"));
        sexCol.setCellValueFactory(new PropertyValueFactory<>("gioi_tinh"));
        pNumCol.setCellValueFactory(new PropertyValueFactory<>("dien_thoai"));
        lCol.setCellValueFactory(new PropertyValueFactory<>("noi_o"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("ho_ten"));

        FilteredList<Employee> empFilteredList= new FilteredList<>(masterData, p -> true);

        search_tf.textProperty().addListener((observable, oldValue, newValue) -> {
            empFilteredList.setPredicate(emp -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                if (emp.getHo_ten().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                if (emp.getUser_name().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<Employee> sortedItem = new SortedList<>(empFilteredList);

        sortedItem.comparatorProperty().bind(listEmp.comparatorProperty());

        listEmp.setItems(sortedItem);


        listEmp.setOnMouseClicked((MouseEvent event)-> {
            if(event.getClickCount() > 0) {
                if(listEmp.getSelectionModel().getSelectedItem() != null) {
                    updateBtn.setDisable(false);
                    deleteBtn.setDisable(false);
                    emp = listEmp.getSelectionModel().getSelectedItem();
                    if(event.getClickCount() > 1) {
                        new View("/sample/Resources/FXML/updateEmpForm.fxml");
                        updateBtn.setDisable(true);
                    }
                }
            }
        });
    }


    public void setDataForList()  {
        try {
            con = ConnectionClass.getInstances().getConnection();
            String sql = "select l.user_name,l.password,c.ten_chuc_vu, t.gioi_tinh,t.dien_thoai,t.noi_o,t.ho_ten from login l join chuc_vu c on c.id = l.Chuc_Vu_id join thong_tin_chi_tiet t on t.Login_user_name = l.user_name ;";
            PreparedStatement ptsmt = con.prepareStatement(sql);
            ResultSet rs = ptsmt.executeQuery();
            masterData.clear();
            while(rs.next()) {
             masterData.add(new Employee(rs.getString(1), rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5), rs.getString(6),rs.getString(7)));

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public AnchorPane getPane() {
        return pane;
    }
}
