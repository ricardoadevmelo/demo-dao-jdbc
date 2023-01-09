package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {

    private static Connection connection = null;

    public static Connection getConnection(){
        if (connection == null) {
            try {
                Properties properties = loadProperties();
                String url = properties.getProperty("dburl");
                connection = DriverManager.getConnection(url, properties);
            }
            catch (SQLException sqlException){
                throw new DbException(sqlException.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection(){
        try {
            if (connection != null) {
                connection.close();
            }
        }
        catch (SQLException sqlException){
            throw new DbException(sqlException.getMessage());
        }
    }

    private static Properties loadProperties(){
        try (FileInputStream fileInputStream = new FileInputStream("db.properties")){
            Properties properties = new Properties();
            properties.load(fileInputStream);
            return properties;
        }
        catch (IOException iioException){
            throw new DbException(iioException.getMessage());
        }
    }

    public static void closeStatement(Statement statement){
        if (statement != null) {
            try {
                statement.close();
            }
            catch (SQLException sqlException){
                throw new DbException(sqlException.getMessage());
            }
        }
    }

    public static void closeResultSet(ResultSet resultSet){
        if (resultSet != null) {
            try {
                resultSet.close();
            }
            catch (SQLException sqlException){
                throw new DbException(sqlException.getMessage());
            }
        }
    }
}
