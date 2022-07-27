package edu.school21.reflection.classes;

import java.util.StringJoiner;

public class Car {
    private String model;
    private String color;
    private int price;
    private boolean automated;

    public Car() {
        this.model = "Default";
        this.color = "black";
        this.price = 0;
        this.automated = false;
    }

    public Car(String model, String color, int price, boolean automated) {
        this.model = model;
        this.color = color;
        this.price = price;
        this.automated = automated;
    }

    public boolean ifAutomated() {
        return automated;
    }

    public int changePrice(int amount, boolean increase) {
        if (increase) {
            price += amount;
        } else {
            price -= amount;
        }
        return price;
    }

    public String changeColor(String color) {
        this.color = color;
        return color;
    }

    public void changeModel(String model) {
        this.model = model;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Car.class.getSimpleName() + "[", "]")
                . add("model='" + model + "'")
                . add("color='" + color + "'")
                . add("price='" + price + "'")
                . add("automated=" + (automated ? "yes" : "no"))
                . toString() ;
    }


}
