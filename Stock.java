package com.example.test7;



import java.io.Serializable;

// Make sure Stock class also implements Serializable
public class Stock implements Serializable {
    private String name;
    private double price;
    private String description;

    public Stock(String name, double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public String getRiskLevel() {
        // Implement risk level logic if needed
        return "Medium"; // Placeholder
    }
}
