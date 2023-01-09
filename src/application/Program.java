package application;

import db.DB;
import db.DbIntegrityException;
import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Program {
    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=== TEST 1: seller findById ===");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);

        System.out.println("=== TEST 2: seller findByDepartment ===");
        Department department = new Department(2, null);
        List<Seller> sellerList = sellerDao.findByDepartment(department);
        for (Seller obj : sellerList){
            System.out.println(obj);
        }

        System.out.println("=== TEST 3: seller findByAll ===");
        sellerList = sellerDao.findAll();
        for (Seller obj : sellerList){
            System.out.println(obj);
        }
    }
}