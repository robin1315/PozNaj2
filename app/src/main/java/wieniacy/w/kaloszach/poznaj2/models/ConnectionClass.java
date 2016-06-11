package wieniacy.w.kaloszach.poznaj2.models;

import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Robin on 07.04.2016.
 */
public class ConnectionClass {
    /*
TCP/IP Server : mn11.webd.pl
Port : 3306
User: walenmar_poznaj
Password: Polibuda2016
Database: walenmar_poznaj

     */
    public Connection conn;
    public ResultSet result;
    public ConnectionClass() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        conn = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://mn11.webd.pl:3306/walenmar_poznaj","walenmar_poznaj", "Polibuda2016");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Connection getConn() {
        return conn;
    }

    public ResultSet getResult() {
        return result;
    }

    public ResultSet makeQuery(String query) throws SQLException {
        result = null;
        try {
            Statement stmt = (Statement) conn.createStatement();
            result = (ResultSet) stmt.executeQuery(query);

        }catch (SQLException e){
            e.printStackTrace();
        }

        return result;
    }
    public void makeUpdate(String query) throws SQLException{
        try{
            Statement stmt = (Statement) conn.createStatement();
            stmt.executeUpdate(query);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
