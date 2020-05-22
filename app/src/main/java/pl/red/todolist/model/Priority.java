package pl.red.todolist.model;

import static pl.red.todolist.R.drawable.priority_high;
import static pl.red.todolist.R.drawable.priority_low;
import static pl.red.todolist.R.drawable.priority_medium;
import static pl.red.todolist.R.id.high;
import static pl.red.todolist.R.id.low;
import static pl.red.todolist.R.id.medium;

public enum Priority {
    LOW(priority_low, low), MEDIUM(priority_medium, medium), HIGH(priority_high, high);


    Priority(int color, int id) {
        this.color = color;
        this.id = id;
    }

    private final int color;
    private final int id;

    public int getColor() {
        return color;
    }

    public int getId() {
        return id;
    }
}
