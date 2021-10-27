package com.dkit.oopca5.BusinessObjects;

import com.dkit.oopca5.Exceptions.DaoException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws DaoException {

        String url = "jdbc:mysql://localhost/";
        String dbName = "oop_ca5_amrita_giri";
//            String driver = "com.mysql.cj.jdbc.Driver";
        String userName = "root";
        String password = "";
        try {
            Connection conn = DriverManager.getConnection(url + dbName, userName, password);
            System.out.println("connected");

            com.dkit.oopca5.DAOs.MySqlStudentCoursesDao studentc = new com.dkit.oopca5.DAOs.MySqlStudentCoursesDao();
            studentc.getStudentChoices(12345678);

            conn.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }
}
