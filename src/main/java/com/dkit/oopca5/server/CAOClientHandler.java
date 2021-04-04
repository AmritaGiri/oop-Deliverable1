package com.dkit.oopca5.server;

/*
The CAOClientHandler will run as a thread. It should listen for messages from the Client and respond to them.There should be one CAOClientHandler per Client.
 */

import com.dkit.oopca5.DTOs.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CAOClientHandler {
    public static void main(String[] args) {

        /// get rid of the student manager - as its services have been replaced by the DAO
        //
        ///StudentManager studentManager = new StudentManager();

        StudentDaoInterface studentDao;
        studentDao = new MySqlStudentDao();

        try {

            int caoNum = 55551111;  // get from user input
            String dob = "1999-01-22";
            String pw = "X3e4r5";
            studentDao.registerStudent(new Student(caoNum, dob, pw));

            //Student searchKey = new Student(caoNum,"","");  // student to search for
            Student result = studentDao.findStudent(55551111);  // get student based on CAO number
            System.out.println("Student::" + result);

            List<Student> studentList = studentDao.findAllStudents();
            // loop to print all students

        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    private static class MySqlStudentDao implements StudentDaoInterface {
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

                String query = "SELECT * FROM STUDENTS";
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
}









