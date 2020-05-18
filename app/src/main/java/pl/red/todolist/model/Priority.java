package pl.red.todolist.model;

import static pl.red.todolist.R.drawable.priority_high;
import static pl.red.todolist.R.drawable.priority_low;
import static pl.red.todolist.R.drawable.priority_medium;

enum Priority {
    LOW(priority_low), MEDIUM(priority_medium), HIGH(priority_high);


    Priority(int color) {
        this.color = color;
    }

    private int color;

    public int getColor() {
        return color;
    }
}
