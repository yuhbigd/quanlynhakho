package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {

    private static ConnectionClass instance = null;
    private final Connection con;


    private ConnectionClass() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "password");
    }

    public static ConnectionClass getInstances() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new ConnectionClass();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        return getInstances().con;
    }
}
