package application;

import db.DB;
import db.DbIntegrityException;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Program {
    public static void main(String[] args) {

        Connection connection = null;
        PreparedStatement pSt = null;

        try {
            connection = DB.getConnection();
            pSt = connection.prepareStatement(
                    "DELETE FROM department "
                            + "WHERE "
                            + "Id = ?");

            pSt.setInt(1, 5);

            int rowsAffected = pSt.executeUpdate();

            System.out.println("Done! Rows affected: " + rowsAffected);
        }
        catch (SQLException sqlException){
            throw new DbIntegrityException(sqlException.getMessage());
        }
        finally {
            DB.closeStatement(pSt);
            DB.closeConnection();
        }
    }
}