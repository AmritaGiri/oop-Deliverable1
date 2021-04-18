package com.dkit.oopca5.DAOs;

import com.dkit.oopca5.DTOs.Course;
import com.dkit.oopca5.Exceptions.DaoException;

import java.util.List;

public interface CourseDaoInterface {
    public List<Course> displayAllCourses() throws DaoException;

    public void displayCourse(String cao) throws DaoException;
}
