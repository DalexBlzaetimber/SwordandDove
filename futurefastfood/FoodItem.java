package com.example.futurefastfood;

public class FoodItem {
    private String name;
    private double price;
    private int image;

    public FoodItem(String name, double price, int image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getImage() {
        return image;
    }
}
