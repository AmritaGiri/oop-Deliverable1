package com.dkit.oopca5.client;

import com.dkit.oopca5.DAOs.MySqlCourseDao;
import com.dkit.oopca5.DAOs.MySqlStudentCoursesDao;
import com.dkit.oopca5.DAOs.MySqlStudentDao;
import com.dkit.oopca5.Exceptions.DaoException;
import com.dkit.oopca5.Menus.CAOCourseMenu;
import com.dkit.oopca5.Menus.MainMenu;
import com.dkit.oopca5.core.CAOService;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;


/*This class will print out the menu.*/
public class Client {

    private Scanner keyboard = new Scanner(System.in);
    private Socket dataSocket = new Socket(CAOService.HOSTNAME, CAOService.PORT_NUM);
    private String response = "";


    public Client() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.start();
    }

    public void start() {
        doMainMenuLoop();

        Scanner in = new Scanner(System.in);
        try {
            Socket socket = new Socket("localhost", 8080);  // connect to server socket

            System.out.println("Client message: The Client is running and has connected to the server");

            System.out.println("Please enter a command:  (\"Time\" to get time, or \"Echo message\" to get echo) \n>");
            String command = in.nextLine();

            OutputStream os = socket.getOutputStream();
            PrintWriter out = new PrintWriter(os, true);

            out.write(command + "\n");  // write command to socket, and newline terminator
            out.flush();              // flush (force) the command over the socket

            Scanner inStream = new Scanner(socket.getInputStream());  // wait for, and retrieve the reply

            if (command.startsWith("Time"))   //we expect the server to return a time (in milliseconds)
            {
                String timeString = inStream.nextLine();
                System.out.println("Client message: Response from server Time: " + timeString);
            } else                            // the user has entered the Echo command or an invalid command
            {
                String input = inStream.nextLine();
                System.out.println("Client message: Response from server: \"" + input + "\"");
            }

            out.close();
            inStream.close();
            socket.close();

        } catch (IOException e) {
            System.out.println("Client message: IOException: " + e);
        }
    }

    public void doMainMenuLoop() {
        try {
            //Step 1: Establish a connection to the server
            // Like a phone call, first need to dial the number
            // before you can talk
//            Socket dataSocket = new Socket(CAOService.HOSTNAME, CAOService.PORT_NUM);

            //Step 2: Build input and output streams
            OutputStream out = dataSocket.getOutputStream();
            //Decorator pattern
            PrintWriter output = new PrintWriter(new OutputStreamWriter(out));

            InputStream in = dataSocket.getInputStream();
            Scanner input = new Scanner(new InputStreamReader(in));

//            Scanner keyboard = new Scanner(System.in);

            String message = "";
            MainMenu choice;
            int getChoice;

            while (!message.equals(CAOService.END_SESSION)) {
                displayMainMenu();
                getChoice = keyboard.nextInt();
                keyboard.nextLine();
                choice = MainMenu.values()[getChoice];
//                String response = "";

                switch (choice) {
                    case QUIT_APPLICATION:
                        message = CAOService.END_SESSION;
                        //send message
                        output.println(message);
                        output.flush();

                        //Listen for response
                        response = input.nextLine();
                        if (response.equals(CAOService.SESSION_TERMINATED)) {
                            System.out.println("Session ended");
                        }
                        break;
                    case REGISTER:
                        message = register(keyboard);

                        //send message
                        output.println(message);
                        output.flush();
                        //listen for response
                        response = input.nextLine();
                        System.out.println("Received response: " + response);
                        break;

                    case LOGIN:
                        message = login(keyboard);
                        //send message
                        output.println(message);
                        output.flush();
                        //listen for response
                        response = input.nextLine();  //Doesn't like .next OR .nextLine. Find out why.
                        System.out.println("Received response: " + response);
                        break;
                }
                if (response.equals(CAOService.UNRECOGNISED)) {
                    System.out.println("Sorry, the request is not recognised.");
                }
            }
            System.out.println("Thanks for using the CAO Application App.");
            dataSocket.close();
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (DaoException throwables) {
            throwables.printStackTrace();
        }
    }

    private void doAdminMenuLoop(MySqlStudentDao loginResult, int caoNum) throws IOException {

        OutputStream out = dataSocket.getOutputStream();

        //Decorator pattern
        PrintWriter output = new PrintWriter(new OutputStreamWriter(out));

        InputStream in = dataSocket.getInputStream();
        Scanner input = new Scanner(new InputStreamReader(in));
        String message = "";
        boolean loop = true;
        CAOCourseMenu menuOption;
        int option;
        try {

            while (!message.equals(CAOService.LOGOUT)) {
                printCaoCourseMenu();

                option = keyboard.nextInt();
                keyboard.nextLine();
                menuOption = CAOCourseMenu.values()[option];
                switch (menuOption) {
                    case QUIT:
                        message = CAOService.END_SESSION;
                        //send message
                        output.println(message);
                        output.flush();

                        //Listen for response
                        response = input.nextLine();
                        if (response.equals(CAOService.SESSION_TERMINATED)) {
                            System.out.println("Session ended");
                        }
                        break;
                    case LOGOUT:
                        message = CAOService.LOGOUT;
                        //send message
                        output.println(message);
                        output.flush();
                        //listen for response
                        response = input.nextLine();
                        System.out.println("Received response: " + response);
                        doMainMenuLoop();
                        break;
                    case DISPLAY_COURSE:
                        message = CAOService.DISPLAY_COURSE;
                        output.println(message);
                        output.flush();


                        System.out.println("Enter Course ID");
                        String courseID = keyboard.nextLine();
                        MySqlCourseDao getacourse = new MySqlCourseDao();
                        getacourse.displayCourse(courseID);


                        break;
                    case DISPLAY_ALL_COURSES:
                        MySqlCourseDao allcourse = new MySqlCourseDao();
                        allcourse.displayAllCourses();
//                        displayAllCourses();
                        break;
                    case DISPLAY_CURRENT_CHOICES:
                        MySqlStudentCoursesDao current = new MySqlStudentCoursesDao();
                        current.getStudentChoices(caoNum);
                        break;
                    case UPDATE_CURRENT_CHOICES:
                        MySqlStudentCoursesDao pickchoices = new MySqlStudentCoursesDao();
                        pickchoices.updatechoices(caoNum);
                        break;
                }
                if (response.equals(CAOService.UNRECOGNISED)) {
                    System.out.println("Sorry, the request is not recognised.");
                }
            }


        } catch (InputMismatchException ime) {
            System.out.println("InputMismatchException. The token retrieved does not match the pattern for the expected type. Please enter a valid option");
            keyboard.nextLine();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ArrayIndexOutOfBoundsException. The index is either negative or greater than or equal to the size of the array.");
            keyboard.nextLine();
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException. A method has been passed an illegal argument. Try again");
            keyboard.nextLine();
        } catch (DaoException throwables) {
            throwables.printStackTrace();
        }
    }

    private String login(Scanner input) throws DaoException, IOException {
        System.out.println("Login: ");

        StringBuffer message = new StringBuffer(CAOService.LOGIN_COMMAND);
        String output = "";
        message.append(CAOService.BREAKING_CHARACTER);
        System.out.println("Enter CAO Number:\t");
        int caoNumber = input.nextInt();
        System.out.println("Enter Password:\t");
        String password = input.next();
        MySqlStudentDao login = new MySqlStudentDao();
        boolean cool = false;


        if (login.login(caoNumber, password)) {
            output = CAOService.SUCCESSFUL_LOGIN;
            if (output == CAOService.SUCCESSFUL_LOGIN) {
                cool = true;
            }
            while (cool = true) {

//            output = CAOService.SUCCESSFUL_LOGIN;
                System.out.println("Logged in");
                doAdminMenuLoop(login, caoNumber);
            }
        }
//        message.append(login);

        boolean idc;
        String result2 = "";
        if (result2.equals(login.login(caoNumber, password))) {

            doAdminMenuLoop(login, caoNumber);
        }
        message.append(result2);

        return message.toString();

    }

    public String register(Scanner input) throws DaoException {

        StringBuffer message = new StringBuffer(CAOService.REGISTER_COMMAND);
        message.append(CAOService.BREAKING_CHARACTER);
        System.out.println("Enter CAO Number:");
        int caoNumber;
        caoNumber = input.nextInt();
        message.append(caoNumber);

        System.out.println("Enter Date of Birth:");
        String dateOfBirth = input.next();
        message.append(CAOService.BREAKING_CHARACTER);
        message.append(dateOfBirth);
        System.out.println("Enter Password:");
        String password = input.next();
        message.append(CAOService.BREAKING_CHARACTER);
        message.append(password);
        //String echo = input.nextLine();
        MySqlStudentDao register = new MySqlStudentDao();
        String result1 = register.register(caoNumber, dateOfBirth, password);
        message.append(result1);

        return message.toString();
    }

    private void displayMainMenu() {
        System.out.println("\n Options to select:");
        for (int i = 0; i < MainMenu.values().length; i++) {
            System.out.println("\t" + i + ". " + MainMenu.values()[i].toString());
        }
        System.out.print("Enter a number to select option (enter 0 to quit):>");
    }

    private void printCaoCourseMenu() {
        System.out.println("CAO Course Menu.");
        System.out.println("\n Select One The Following Options: ");
        for (int i = 0; i < CAOCourseMenu.values().length; i++) {
            System.out.println("\t" + i + ". " + CAOCourseMenu.values()[i].toString());
        }
        System.out.print("Enter a number to select option (enter 0 to quit):>");
    }

}





























































