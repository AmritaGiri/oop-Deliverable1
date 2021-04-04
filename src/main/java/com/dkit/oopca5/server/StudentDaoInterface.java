package com.dkit.oopca5.server;


import com.dkit.oopca5.DTOs.Student;

import java.util.List;

public interface StudentDaoInterface {
    public List<Student> findAllStudents() throws DaoException;

    public boolean registerStudent(Student s) throws DaoException;

    public Student findStudent(int caoNumber) throws DaoException;
}





