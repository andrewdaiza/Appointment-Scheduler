package Utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 *
 * @author andrew.daiza
 */
public class DBConnection {
    private static final String databaseName="U07oLb";
    private static final String DB_URL="jdbc:mysql://3.227.166.251/U07oLb";
    private static final String username="U07oLb";
    private static final String password="53689088013";
    private static final String driver="com.mysql.jdbc.Driver";
    static Connection conn = null;
    public static Connection makeConnection()throws ClassNotFoundException, SQLException, Exception
    {
        Class.forName(driver);
        conn=(Connection) DriverManager.getConnection(DB_URL,username,password);
        System.out.println("Connection Successful");
        return conn;
    }
    public static Connection getConnection()
    {
        return conn;
    }
    public static Connection closeConnection()throws ClassNotFoundException, SQLException, Exception{
        conn.close();
        System.out.println("Connection Closed");
        return conn;
    }
}

