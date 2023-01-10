package model.dao.imple;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {

    Connection connection;

    public DepartmentDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Department department) {
        PreparedStatement pSt = null;

        try {
            pSt = connection.prepareStatement(
                    "INSERT INTO department "
                            + "(Name) "
                            + "VALUES "
                            + "(?)",
                            Statement.RETURN_GENERATED_KEYS);

            pSt.setString(1, department.getName());

            int rowsAffected = pSt.executeUpdate();

            if (rowsAffected > 0) {

                ResultSet resultSet = pSt.getGeneratedKeys();

                if (resultSet.next()){
                    int id = resultSet.getInt(1 );
                    department.setId(id);
                }
                DB.closeResultSet(resultSet);
            }
            else {
                throw new DbException("Unexpected error! No rows affected!");
            }
        }
        catch (SQLException sqlException){
            throw new DbException(sqlException.getMessage());
        }
        finally {
            DB.closeStatement(pSt);
        }
    }

    @Override
    public void update(Department department) {
        PreparedStatement pSt = null;

        try {
            pSt = connection.prepareStatement(
                    "UPDATE department "
                            + "SET Name = ? "
                            + "WHERE Id = ?");

            pSt.setString(1, department.getName());
            pSt.setInt(2, department.getId());

            pSt.executeUpdate();
        }
        catch (SQLException sqlException){
            throw new DbException(sqlException.getMessage());
        }
        finally {
            DB.closeStatement(pSt);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement pSt = null;

        try {
            pSt = connection.prepareStatement("DELETE FROM department WHERE Id = ?");

            pSt.setInt(1, id);

            pSt.executeUpdate();
        }
        catch (SQLException sqlException){
            throw new DbException(sqlException.getMessage());
        }
        finally {
            DB.closeStatement(pSt);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement pSt = null;
        ResultSet resultSet = null;

        try {
            pSt = connection.prepareStatement(
                    "SELECT * FROM department WHERE Id = ?");

            pSt.setInt(1, id);
            resultSet = pSt.executeQuery();

            if (resultSet.next()) {
                Department department = new Department();
                department.setId(resultSet.getInt("Id"));
                department.setName(resultSet.getString("Name"));
                return department;
            }
            return null;
        }
        catch (SQLException sqlException){
            throw new DbException(sqlException.getMessage());
        }
        finally {
            DB.closeStatement(pSt);
            DB.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement pSt = null;
        ResultSet resultSet = null;

        try {
            pSt = connection.prepareStatement("SELECT * FROM department ORDER BY Name");
            resultSet = pSt.executeQuery();

            List<Department> departmentList = new ArrayList<>();

            while (resultSet.next()){
                Department department = new Department();
                department.setId(resultSet.getInt("Id"));
                department.setName(resultSet.getString("Name"));
                departmentList.add(department);
            }
            return departmentList;
        }
        catch (SQLException sqlException){
            throw new DbException(sqlException.getMessage());
        }
        finally {
            DB.closeStatement(pSt);
            DB.closeResultSet(resultSet);
        }
    }
}
