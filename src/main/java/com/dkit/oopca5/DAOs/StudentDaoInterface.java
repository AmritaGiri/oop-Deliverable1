package com.dkit.oopca5.DAOs;


import com.dkit.oopca5.DTOs.Student;
import com.dkit.oopca5.Exceptions.DaoException;

import java.util.List;

public interface StudentDaoInterface
{
    public List<Student> findAllStudents() throws DaoException;

    public Student findStudentByCaoNumberPassword(int caoNumber, String password) throws DaoException;

    public String register(int caoNumber, String dateOfBirth, String password) throws DaoException;

    public boolean login(int caoNumber, String password) throws DaoException;
}
