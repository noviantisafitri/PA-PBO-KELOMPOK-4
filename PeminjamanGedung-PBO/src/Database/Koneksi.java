/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Database;
import java.sql.*;


/**
 *
 * @author Novi
 */

public abstract class Koneksi implements Database {
    protected static Connection connection = null;
    protected static Statement statement;
    protected static ResultSet resultSet;
    protected static PreparedStatement preparedStatement;
    private static String query;
    
    public Koneksi() {
        openConnection();
    }
    
    protected static final void openConnection() {
        try {
            final String url = "jdbc:mysql://localhost/peminjaman_gedung?user=root&password=";
            
            connection = DriverManager.getConnection(url);
            
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
    }
    
    protected static final void closeConnection() {
        try {
            if  (resultSet != null) resultSet.close();
            if  (statement != null) statement.close();
            if  (preparedStatement != null) preparedStatement.close();
            if  (connection != null) connection.close();
            
            resultSet = null;
            statement = null;
            preparedStatement = null;
            connection = null;
        } catch (SQLException ex) {}
    }
    
    protected static final void displayErrors(SQLException ex){
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
    }
    
    protected static final Connection getConnection() {
        openConnection();
        return connection;
    }

    protected static final Statement getStatement() {
        return statement;
    }

    protected static final ResultSet getResultSet() {
        return resultSet;
    }

    protected static final PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

    protected static final void setStatement(Statement stm) {
        statement = stm;
    }
    
    @Override
    public void setQuery(String sql) {
        query = sql;
    }

    @Override
    public String getQuery() {
        return query;
    }
    
    
}