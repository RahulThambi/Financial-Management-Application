package com.example.test7;

import java.util.Date;

public class Transaction {
    private String name;
    private Date date;

    private String category;
    private double amount;
    private boolean isExpense;
    public Date getDate() {
        return date;
    }

    public Transaction(String name, String category, double amount, boolean isExpense) {

        this.name = name;
        this.category = category;
        this.amount = amount;
        this.isExpense = isExpense;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isExpense() {
        return isExpense;
    }

    @Override
    public String toString() {
        return String.format("%s - %s: $%.2f (%s)",
                name, category, amount, isExpense ? "Expense" : "Savings");
    }
}