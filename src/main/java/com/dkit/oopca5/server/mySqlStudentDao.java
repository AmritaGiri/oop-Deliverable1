package com.dkit.oopca5.server;
/**
 * OOP 2021
 * <p>
 * Data Access Object (DAO) for User table with MySQL-specific code
 * This 'concrete' class implements the 'UserDaoInterface'.
 * <p>
 * The DAO will contain the SQL query code to interact with the database,
 * so, the code here is specific to a particular database (e.g. MySQL or Oracle etc...)
 * No SQL queries will be used in the Business logic layer of code, thus, it
 * will be independent of the database specifics.
 */

import com.dkit.oopca5.DTOs.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class mySqlStudentDao implements StudentDaoInterface {
    private Connection connection;

    @Override
    public List<Student> findAllStudents() throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Student> students = new ArrayList<>();

        try {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();

            String query = "SELECT * FROM S";
            ps = con.prepareStatement(query);

            //Using a PreparedStatement to execute SQL...
            rs = ps.executeQuery();
            while (rs.next()) {
                int caoNumber = rs.getInt("caoNumber");
                String dob = rs.getString("dateOfBirth");
                String password = rs.getString("password");

                Student stu = new Student(caoNumber, dob, password);
                students.add(stu);
            }
        } catch (SQLException e) {
            throw new DaoException("findAllStudents() " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    connection(con);
                }
            } catch (SQLException e) {
                throw new DaoException("findAllStudents() " + e.getMessage());
            }
        }
        return students;     // may be empty
    }

    private void connection(Connection con) {
    }

    public boolean registerStudent(Student s) throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean success = false;

        try {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            con = this.getConnection();

            String query = "INSERT INTO STUDENT VALUES (?,?,?)";
            ps = con.prepareStatement(query);

            ps.setInt(1, s.getCaoNumber());
            ps.setString(2, s.getDateOfBirth());
            ps.setString(3, s.getPassword());


            //Using a PreparedStatement to execute SQL - UPDATE...
            success = (ps.executeUpdate() == 1);

        } catch (SQLException e) {
            throw new DaoException("insertStudent() " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    connection(con);
                }
            } catch (SQLException e) {
                throw new DaoException("insertStudent() " + e.getMessage());
            }
        }
        return success;
    }


    public Student findStudent(int caoNumber) throws DaoException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Student s = null;

        try {
            con = this.getConnection();

            String query = "SELECT * FROM Student WHERE caoNumber = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, caoNumber);  // search based on the cao number

            rs = ps.executeQuery();
            if (rs.next()) {
                caoNumber = rs.getInt("CAONUMBER");
                String dateOfBirth = rs.getString("DATEOFBIRTH");
                String password = rs.getString("PASSWORD");

                s = new Student(caoNumber, dateOfBirth, password);
            }
        } catch (SQLException e) {
            throw new DaoException("findStudentByUsernamePassword() " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    connection(con);
                }
            } catch (SQLException e) {
                throw new DaoException("findStudentByUsernamePassword() " + e.getMessage());
            }
        }
        return s;     // s may be null
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}














