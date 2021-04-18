package com.dkit.oopca5.DAOs;

import com.dkit.oopca5.Exceptions.DaoException;

import java.util.List;

public interface StudentCourseInterfaceDao {
    public List<String> getStudentChoices(int cao) throws DaoException;

    public void updatechoices(int cao) throws DaoException;

}
