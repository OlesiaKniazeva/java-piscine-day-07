package edu.school21.reflection.app;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class Program {
    private static final String CLASS_NAME_1 = "User";
    private static final String CLASS_NAME_2 = "Car";
    private static final String DELIMITER = "-------------------";
    private static final String CLASSES_PATH = "edu.school21.reflection.classes";

    public static void main(String[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Scanner sc = new Scanner(System.in);
        DataHandler handler = new DataHandler(CLASSES_PATH, sc);

        System.out.println("Classes:\n   -  " + CLASS_NAME_1 + "\n   -  " + CLASS_NAME_2);
        System.out.println(DELIMITER);

        System.out.println("Enter class name:");
        String enteredName = sc.next();

        handler.classToHandle(enteredName);

        handler.printFields();
        System.out.println(DELIMITER);
        handler.printMethods();
        System.out.println(DELIMITER);

        System.out.println("Lets create an object.");
        handler.createNewInstance();
        System.out.println(DELIMITER);

        System.out.println("Enter name of the field for changing: ");
        String fieldToChange = sc.next();
        handler.changeName(fieldToChange);
        System.out.println(DELIMITER);

        System.out.println("Enter name of the method for call:");
        String methodName = sc.next();
        handler.callMethod(methodName);
    }
}
