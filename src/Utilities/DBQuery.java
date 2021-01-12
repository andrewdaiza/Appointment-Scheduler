package Utilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author andrew.daiza
 */
public class DBQuery {
        
    private static PreparedStatement statement; 
    
        
            // Create prepared Statement object
        public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException {
            statement = conn.prepareStatement(sqlStatement);
        }
    
        // return Statment Object
        
        public static Statement getStatement() {
            return statement;
        }
        
          // return Prepared Statment Object
        
        public static PreparedStatement getPreparedStatement() {
            return statement;
        }

    public static void setStatement(Connection conn) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
