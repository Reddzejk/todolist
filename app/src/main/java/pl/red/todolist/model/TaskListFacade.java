package pl.red.todolist.model;

import android.net.Uri;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import pl.red.todolist.utils.Order;

public class TaskListFacade {
    private final List<Task> tasks;

    public TaskListFacade() {
        this.tasks = new LinkedList<>();
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

    public LocalDateTime getDeadlineDateTimeTask(int position) {
        return tasks.get(position).getDeadlineDate();
    }

    public LocalDateTime getCompletedDataTimeTask(int position) {
        return tasks.get(position).getCompleteDate();
    }


    public int getPriorityColorTask(int position) {
        return tasks.get(position).getPriority().getColor();
    }

    public String getPriorityNameTask(int position) {
        return tasks.get(position).getPriority().name();
    }

    public boolean getStatusTask(int position) {
        return tasks.get(position).isFinished();
    }

    public int count() {
        return tasks.size();
    }

    public void setStatusTask(int position, boolean isChecked) {
        tasks.get(position).setFinished(isChecked);
        if (isChecked) {
            tasks.get(position).setCompleteIfEmpty(LocalDateTime.now());
        } else {
            tasks.get(position).resetCompleted();
        }
    }

    public String getMessageAfterDeadlineTasks() {
        LocalDateTime notificationDate = LocalDate.now().plusDays(1).atStartOfDay();

        return tasks.stream()
                .filter(task -> !task.isFinished() && task.getDeadlineDate().isBefore(notificationDate))
                .map(Task::getTitle)
                .collect(Collectors.joining("\n"));
    }

    public void sortTitle(Order asc) {
        sort(asc, (o1, o2) -> o1.getTitle().compareTo(o2.getTitle()));
    }


    public void sortDeadline(Order order) {
        sort(order, (o1, o2) -> o1.getDeadlineDate().compareTo(o2.getDeadlineDate()));
    }

    public void sortPriority(Order order) {
        sort(order, (o1, o2) -> o1.getPriority().compareTo(o2.getPriority()));
    }

    public void sortStatus(Order order) {
        sort(order, (o1, o2) -> o1.isFinished().compareTo(o2.isFinished()));
    }

    private void sort(Order order, Comparator<Task> asc) {
        tasks.sort(asc);
        if (order == Order.DESC) {
            Collections.reverse(tasks);
        }
    }

    public List<String> lines() {
        return tasks.stream()
                .map(Task::toString)
                .collect(Collectors.toList());
    }

    public String getDescriptionTask(int position) {
        return tasks.get(position).getDescription();
    }

    public void replaceTask(int position, Task result) {
        tasks.set(position, result);
    }

    public byte[] getSerialized() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(tasks);
        return bos.toByteArray();
    }

    public void deserialize(byte[] loadData) {
        if (loadData.length == 0) {
            return;
        }
        ByteArrayInputStream bos = new ByteArrayInputStream(loadData);
        ObjectInputStream oos;
        try {
            oos = new ObjectInputStream(bos);
            tasks.addAll((Collection<? extends Task>) oos.readObject());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Uri> getAttachmentsUri(int position) {
        return tasks.get(position).getAttachmentsUri();
    }
}
