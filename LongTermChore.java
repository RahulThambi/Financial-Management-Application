package com.example.test7;


import java.util.Date;

public class LongTermChore extends Chore {
    private Date deadline;

    public LongTermChore(String name, Date deadline, double income) {
        super(name, income);
        this.deadline = deadline;
    }

    public Date getDeadline() {
        return deadline;
    }

    @Override
    public int getProgress() {
        long now = System.currentTimeMillis();
        long total = deadline.getTime() - now;
        long elapsed = System.currentTimeMillis() - now;
        return (int) (elapsed * 100 / total);
    }
}
