package pl.red.todolist.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import pl.red.todolist.model.Task;

public class TaskList {
    private final Map<UUID, Task> tasks;

    public TaskList() {
        this.tasks = new HashMap<>();
    }

    public TaskList(Map<UUID, Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void deleteTask(UUID task) {
        tasks.remove(task);
    }

    public void  getViewTasks() {
        //TODO: Only values without references
    }
}
