package com.dkit.oopca5.server;


import com.dkit.oopca5.DTOs.Student;

import java.io.*;
import java.util.*;

public class StudentManager {

    //private ArrayList<Student> studentMap;
    private static HashMap<Integer, Student> studentMap;

    // Store all students in data structure
    public StudentManager() {

        studentMap.put(222331, new Student(222331, "1998-03-15", "pass1"));
        studentMap.put(222332, new Student(222332, "1998-03-14", "pass2"));
        studentMap.put(222333, new Student(222333, "1998-03-13", "pass3"));
        studentMap.put(222334, new Student(222334, "1998-03-12", "pass4"));
        studentMap.put(222335, new Student(222335, "1998-03-11", "pass5"));
        studentMap.put(222336, new Student(222336, "1998-03-10", "pass6"));
        studentMap.put(222337, new Student(222337, "1998-03-09", "pass7"));
        studentMap.put(222338, new Student(222338, "1998-03-08", "pass8"));
        studentMap.put(222339, new Student(222339, "1998-03-07", "pass9"));
        studentMap.put(222340, new Student(222340, "1998-03-06", "pass10"));


        StudentDaoInterface studentDao = new mySqlStudentDao();
        try {
            List<Student> studentList = studentDao.findAllStudents();
            //add all students from list to map

            for (Student student : studentList) {
                studentMap.put(student.getCaoNumber(), student);   // [caoNumber->Student]
                System.out.println("StudentMap Dump: " + studentMap);
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }
        studentMap = new HashMap<>();            // instantiate the array list object
    }

    public Student getStudent(int caoNumber) {

        Student student = studentMap.get(caoNumber);
        return new Student(student);  //clone the Student to avoid reference leak

//        for (Student student : studentMap) {
//            if (student.getCaoNumber() == caoNumber) {
//                return new Student(student); // returns a reference to a clone of the student object found
//            }
//        }
//        return null;                     // return the value 'null' indicating not found
    }

    //
    public static void addStudent(Student student) {
        if (student == null) // student could be null. so we need a null check
        {
            throw new IllegalArgumentException();
        } else {
            studentMap.put(student.getCaoNumber(), new Student(student));
        }       // clone student and add the student to list
    }

    public void removeStudent(Student student) // student could be null. so we need a null check
    {
        if (student == null) {
            throw new IllegalArgumentException();
        } else {
            studentMap.remove(student);
        } // clone student and removes the clone from list
    }

    //return list interface type
    public List<Student> getAllStudent() {
        // to store list of cloned student
        ArrayList<Student> clonedList = new ArrayList<>(studentMap.size());

//        for (Student student : studentMap) {
//            clonedList.add(new Student(student));     //avoid leaking references
//        }
//        return clonedList;    // return reference to list of cloned cars
        //iterate through all values in the courseMap and add
        //a copy (a clone) of each course to the new List
        for (Map.Entry<Integer, Student> entry : studentMap.entrySet()) {

            Student student = entry.getValue();
            clonedList.add(new Student(student));
        }
        return clonedList;    // return reference to list of cloned courses

    }

    public void isRegistered(int caoNumber) {

        for (Map.Entry<Integer, Student> entry : studentMap.entrySet()) {
            Student student = entry.getValue();
            if (student.getCaoNumber() == caoNumber)

                System.out.println("Your CAO Number is registered !!! ");
        }
    }


    public void loadStudentsFromFile() {
        //open Students.txt file
        //read Students details and instantiate new Students objects
        // add each new Students object to StudentManager

        System.out.println("Reading Students details from FILE....");
        try {
            Scanner sc = new Scanner(new File("Students.txt"));
            while (sc.hasNext()) {
                int caoNumber = sc.nextInt();
                String dateOfBirth = sc.next();
                String password = sc.next();


                System.out.println("CAO Number: " + caoNumber + "DOB: " + dateOfBirth + "Password: " + password);

                studentMap.put(caoNumber, new Student(caoNumber, dateOfBirth, password));

            }
            System.out.println("All Students details loaded");
            sc.close();
            System.out.println("All Students details: " + studentMap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        catch (IOException e) {
//            System.out.println("IOException thrown in loadStudentsFromFile()"+e.getMessage());
//        }
    }

    public void saveStudentsToFile() throws IOException {
        try (BufferedWriter StudentsFile = new BufferedWriter(new FileWriter("Students.txt"))) {
            for (Map.Entry<Integer, Student> entry : studentMap.entrySet()) {
                Student student = entry.getValue();


                StudentsFile.write(student.getCaoNumber() + "," + student.getDateOfBirth() + "," + student.getPassword());
                StudentsFile.write("\n");

            }
        } catch (IOException e) {
            System.out.println("Could not save Students details !!! ");
        }

    }
}
