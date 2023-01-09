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

public class Program {
    public static void main(String[] args) {

        Department department = new Department(1, "Books");

        Seller seller = new Seller(21, "Bob", "bob@gmail.com", new Date(), 3000.0, department);

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println(seller);

    }
}