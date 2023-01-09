package model.dao;

import model.dao.imple.SellerDaoJDBC;

public class DaoFactory {

    public static SellerDao createSellerDao(){
        return new SellerDaoJDBC();
    }
}
