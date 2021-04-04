package com.dkit.oopca5.DTOs;

import java.util.Objects;

public class StudentCourses {
    private int caoNumber;
    private String courseId;

    public StudentCourses(int caoNumber, String courseId) {
        this.caoNumber = caoNumber;
        this.courseId = courseId;
    }

    public int getCaoNumber() {
        return caoNumber;
    }

    public void setCaoNumber(int caoNumber) {
        this.caoNumber = caoNumber;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Override
    public String toString() {
        return "StudentCourses{" +
                "caoNumber=" + caoNumber +
                ", courseId='" + courseId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentCourses that = (StudentCourses) o;
        return caoNumber == that.caoNumber && courseId.equals(that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caoNumber, courseId);
    }
}
