package model.dao.imple;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection connection;

    public SellerDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement pSt = null;
        try {
            pSt = connection.prepareStatement(
                    "INSERT INTO seller "
                            + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
                            + "VALUES "
                            + "(?, ?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS);

            pSt.setString(1, seller.getName());
            pSt.setString(2, seller.getEmail());
            pSt.setDate(3, new Date(seller.getBirthDate().getTime()));
            pSt.setDouble(4, seller.getBaseSalary());
            pSt.setInt(5, seller.getDepartment().getId());

            int rowsAffected = pSt.executeUpdate();

            if (rowsAffected > 0) {

                ResultSet resultSet = pSt.getGeneratedKeys();

                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    seller.setId(id);
                }
                DB.closeStatement(pSt);
            }
            else {
                throw new DbException("unexpected error! No rows affected!");
            }
        }
        catch (SQLException sqlException){
            throw new DbException(sqlException.getMessage());
        }
    }

    @Override
    public void update(Seller seller) {
        PreparedStatement pSt = null;
        try {
            pSt = connection.prepareStatement(
                    "UPDATE seller "
                            + "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
                            + "WHERE Id = ?");

            pSt.setString(1, seller.getName());
            pSt.setString(2, seller.getEmail());
            pSt.setDate(3, new Date(seller.getBirthDate().getTime()));
            pSt.setDouble(4, seller.getBaseSalary());
            pSt.setInt(5, seller.getDepartment().getId());
            pSt.setInt(6, seller.getId());

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
            pSt = connection.prepareStatement("DELETE FROM seller WHERE Id = ?");

            pSt.setInt(1, id);

            int rows = pSt.executeUpdate();
        }
        catch (SQLException sqlException){
            throw new DbException(sqlException.getMessage());
        }
        finally {
            DB.closeStatement(pSt);
        }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement pSt = null;
        ResultSet resultSet = null;
        try {
            pSt = connection.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "WHERE seller.Id = ?");

            pSt.setInt(1, id);
            resultSet = pSt.executeQuery();
            if (resultSet.next()) {
                Department department = instantiateDepartment(resultSet);
                Seller seller = instantiateSeller(resultSet, department);
                return seller;
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

    private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException{
        Seller seller = new Seller();
        seller.setId(resultSet.getInt("Id"));
        seller.setName(resultSet.getString("Name"));
        seller.setEmail(resultSet.getString("Email"));
        seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
        seller.setBirthDate(resultSet.getDate("BirthDate"));
        seller.setDepartment(department);
        return seller;
    }

    private Department instantiateDepartment(ResultSet resultSet) throws SQLException{
        Department department = new Department();
        department.setId(resultSet.getInt("DepartmentId"));
        department.setName(resultSet.getString("DepName"));
        return department;
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement pSt = null;
        ResultSet resultSet = null;
        try {
            pSt = connection.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "ORDER BY Name");

            resultSet = pSt.executeQuery();

            List<Seller> sellerList = new ArrayList<>();

            Map<Integer, Department> map = new HashMap<>();

            while (resultSet.next()) {

                Department dep = map.get(resultSet.getInt("DepartmentId"));
                if (dep == null) {
                    dep = instantiateDepartment(resultSet);
                    map.put(resultSet.getInt("DepartmentId"), dep);
                }

                Seller seller = instantiateSeller(resultSet, dep);
                sellerList.add(seller);
            }
            return sellerList;
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
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement pSt = null;
        ResultSet resultSet = null;
        try {
            pSt = connection.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "WHERE DepartmentId = ? "
                            + "ORDER BY Name");

            pSt.setInt(1, department.getId());

            resultSet = pSt.executeQuery();

            List<Seller> sellerList = new ArrayList<>();

            Map<Integer, Department> map = new HashMap<>();

            while (resultSet.next()) {

                Department dep = map.get(resultSet.getInt("DepartmentId"));
                if (dep == null) {
                    dep = instantiateDepartment(resultSet);
                    map.put(resultSet.getInt("DepartmentId"), dep);
                }

                Seller seller = instantiateSeller(resultSet, dep);
                sellerList.add(seller);
            }
            return sellerList;
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
