package edu.school21.reflection.app;

import java.lang.reflect.*;
import java.util.*;

public class DataHandler {
    private Class object;
    private Object newInstance;
    private final String CLASSES_PATH;
    private Field[] fields;
    private List<Method> methodsForUser;
    private List<Method> overridenMethods;

    private Scanner sc;

    DataHandler(String path, Scanner sc) {
        CLASSES_PATH = path;
        object = null;
        fields = null;
        methodsForUser = new ArrayList<>();
        overridenMethods = new ArrayList<>();
        this.sc = sc;
        newInstance = null;
    }

    public void classToHandle(String className) {
        try {
            object = Class.forName(CLASSES_PATH + "." + className);
        } catch (ClassNotFoundException e) {
            System.err.println("No such class");
            System.exit(1);
        }
        addFieldsAndMethods();
    }

    private void addFieldsAndMethods() {
        fields = object.getDeclaredFields();

        Method[] methods = object.getDeclaredMethods();
        Method[] overridden = object.getSuperclass().getDeclaredMethods();

        for (Method m : methods) {
            boolean found = false;
            for (Method o : overridden) {
                if (m.getName().equals(o.getName())) {
                    found = true;
                }
            }
            if (found) {
                overridenMethods.add(m);
            } else {
                methodsForUser.add(m);
            }
        }
    }

    public  void printFields() {
        System.out.println("fields: ");
        if (fields.length == 0) {
            System.out.println("    No fields in class");
        } else {
            for (Field f : fields) {
                System.out.println("    " + f.getType().getSimpleName() + " " + f.getName());
            }
        }
    }

    public void printMethods() {
        System.out.println("methods: ");
        if (methodsForUser.size() == 0) {
            System.out.println("    No methods in class");
        } else {
           for (Method m : methodsForUser) {
               System.out.print("    " + m.getReturnType().getSimpleName() + " " + m.getName() + "(");
               Class[] ptypes = m.getParameterTypes();
               for (int i = 0; i < ptypes.length; ++i) {
                   if (i != 0) {
                       System.out.print(", ");
                   }
                   System.out.print(ptypes[i].getSimpleName());
               }
               System.out.println(")");
           }
        }
    }

    public void createNewInstance() throws InvocationTargetException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor constructor = findRightConstructor();
        Object ob = constructor.newInstance();

        for (Field f : object.getDeclaredFields()) {
            System.out.println(f.getName() + ":");
            f.setAccessible(true);
            String newData = sc.next();
            f.set(ob, convertData(f.getType().getSimpleName(), newData));
        }
        newInstance = ob;
        System.out.println("Object created: " + ob);
    }

    private Object convertData(String typeName, String dataToConvert) {
        try {
            switch (typeName.toLowerCase()) {
                case "string":
                    return dataToConvert;
                case "int":
                    return Integer.parseInt(dataToConvert);
                case "double":
                    return Double.parseDouble(dataToConvert);
                case "boolean":
                    if (dataToConvert.equals("yes")) {
                        return true;
                    } else if (dataToConvert.equals("no")) {
                        return false;
                    } else if (dataToConvert.equals("true") | dataToConvert.equals("false")) {
                        return Boolean.parseBoolean(dataToConvert);
                    }
                case "long":
                    return Long.parseLong(dataToConvert);
                default:
                    System.err.println("Wrong data type");
                    System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("Entered wrong data!!!");
            System.exit(1);
        }
        return null;
    }

    private Class convertToClass(String typeName) {
        try {
            switch (typeName.toLowerCase()) {
                case "string":
                    return String.class;
                case "int":
                    return int.class;
                case "double":
                    return double.class;
                case "boolean":
                    return boolean.class;
                case "long":
                    return long.class;
                default:
                    System.err.println("No method");
                    System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("No method found");
            System.exit(1);
        }
        return null;
    }

    private Constructor findRightConstructor() {
        Constructor[] constructors = object.getConstructors();
        for (Constructor c : constructors) {
            if (c.getParameterTypes().length == 0) {
                return c;
            }
        }
        return null;
    }

    public void changeName(String fieldToChange) throws IllegalAccessException {
        try {
            Field f = newInstance.getClass().getDeclaredField(fieldToChange);
            f.setAccessible(true);
            System.out.println("Enter " + f.getType().getSimpleName() + " value:");
            String toChange = sc.next();
            f.set(newInstance, convertData(f.getType().getSimpleName(), toChange));
            System.out.println("Object updated: " + newInstance);
        } catch (NoSuchFieldException e) {
            System.err.println("Field " + fieldToChange + " doesn't exists");
        }
    }

    public void callMethod(String methodName) throws InvocationTargetException, IllegalAccessException {
            Method m = parseMethod(methodName);
            Class[] o = m.getParameterTypes();
            Object[] toAdd = new Object[o.length];
            for (int i = 0; i < o.length; ++i) {
                System.out.println("Enter " + o[i].getSimpleName() + " value:");
                String value = sc.next();
                toAdd[i] = convertData(o[i].getSimpleName(), value);
            }

            Object result = m.invoke(newInstance, toAdd);
            if (Objects.nonNull(result)) {
                System.out.println("Method returned: \n" + result);
            }
    }

    private Class[] makeParams(String[] params) {
        if (params.length < 2) {
            return null;
        }
        Class[] objects = new Class[params.length - 1];

        for (int i = 1; i < params.length; ++i) {
            objects[i - 1] = convertToClass(params[i]);
        }
        return objects;
    }

    public Method parseMethod(String data) {

        try {
            String[] args = data.split("[\\s(),]+");

            if (args.length == 0) {
                throw new NoSuchMethodException();
            }
            Class[] params = makeParams(args);

            Method m = newInstance.getClass().getMethod(args[0], params);
            return m;
        } catch (NoSuchMethodException ex) {
            System.err.println("Method " + data + " doesn't exists");
            System.exit(1);
        }
        return null;
    }
}
