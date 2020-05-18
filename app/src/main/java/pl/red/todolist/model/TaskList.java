package pl.red.todolist.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import pl.red.todolist.utils.Order;

public class TaskList {
    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new LinkedList<>();
    }

    public TaskList(int count) {
        this.tasks = new LinkedList<>();
        Random random = new Random();
        int lastID = 0;
        for (int i = 1; i <= count; i++) {
            Task task = Task.builder()
                    .title("Lorem Ipsum Tytul zadania " + ++lastID)
                    .priority(Priority.values()[random.nextInt(3)])
                    .deadlineDate(LocalDateTime.now().minusDays(random.nextInt(10) + 1))
                    .description("TO JEST OPIS KTÓREGO NNA RAZIE NIE TRZEBA")
                    .build();
            tasks.add(task);
        }
    }

    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(int position) {
        tasks.remove(position);
    }

    public String getTitleTask(int position) {
        return tasks.get(position).getTitle();
    }

    public String getDeadlineDateTask(int position) {
        return tasks.get(position).getDeadlineDate().toLocalDate().toString();
    }

    public int getPriorityColorTask(int position) {
        return tasks.get(position).getPriority().getColor();
    }

    public boolean getStatusTask(int position) {
        return tasks.get(position).isFinished();
    }

    public int count() {
        return tasks.size();
    }

    public void setStatusTask(int position, boolean isChecked) {
        tasks.get(position).setFinished(isChecked);
    }

    public String getAfterDeadline() {
        LocalDateTime notificationDate = LocalDate.now().plusDays(1).atStartOfDay();

        return tasks.stream().filter(task -> !task.isFinished() && task.getDeadlineDate().isBefore(notificationDate))
                .map(Task::getTitle)
                .collect(Collectors.joining("\n"));
    }

    public void sortTitle(Order order) {
        sort(order, (o1, o2) -> o1.getTitle().compareTo(o2.getTitle()), (o1, o2) -> o2.getTitle().compareTo(o1.getTitle()));
    }

    public void sortDeadline(Order order) {
        sort(order, (o1, o2) -> o1.getDeadlineDate().compareTo(o2.getDeadlineDate()), (o1, o2) -> o2.getDeadlineDate().compareTo(o1.getDeadlineDate()));
    }

    public void sortPriority(Order order) {
        sort(order, (o1, o2) -> o1.getPriority().compareTo(o2.getPriority()), (o1, o2) -> o2.getPriority().compareTo(o1.getPriority()));
    }

    public void sortStatus(Order order) {
        sort(order, (o1, o2) -> o1.isFinished().compareTo(o2.isFinished()), (o1, o2) -> o2.isFinished().compareTo(o1.isFinished()));
    }

    private void sort(Order order, Comparator<Task> asc, Comparator<Task> desc) {
        if (order == Order.ASC) {
            tasks.sort(asc);
        } else {
            tasks.sort(desc);
        }
    }

    public List<String> lines() {
        return tasks.stream()
                .map(Task::toString)
                .collect(Collectors.toList());
    }
}
