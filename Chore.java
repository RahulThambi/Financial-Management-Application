package com.example.test7;


public abstract class Chore {
    protected String name;
    protected boolean isCompleted;
    protected double income;

    public Chore(String name, double income) {
        this.name = name;
        this.isCompleted = false;
        this.income = income;
    }

    public String getName() {
        return name;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public double getIncome() {
        return income;
    }

    public abstract int getProgress();
}
