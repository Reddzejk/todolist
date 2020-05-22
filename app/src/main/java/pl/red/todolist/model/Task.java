package pl.red.todolist.model;

import android.net.Uri;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Task implements Serializable {
    private String title;
    private String description;
    private Priority priority;
    private boolean finished;
    private LocalDateTime completeDate;
    private LocalDateTime deadlineDate;
    private final ArrayList<Attachment> attachments;

    private Task() {
        finished = false;
        completeDate = null;
        this.attachments = new ArrayList<>();
    }

    public static Builder builder() {
        return new Builder();
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Priority getPriority() {
        return priority;
    }

    public Boolean isFinished() {
        return finished;
    }

    public LocalDateTime getCompleteDate() {
        return completeDate;
    }

    public LocalDateTime getDeadlineDate() {
        return deadlineDate;
    }

    public List<Uri> getAttachmentsUri() {
        ArrayList<Uri> uris = new ArrayList<>();
        attachments.stream().map(Attachment::getUri).forEach(uris::add);
        return uris;
    }


    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setCompleteIfEmpty(LocalDateTime completeDate) {
        if (this.completeDate == null) {
            this.completeDate = completeDate;
        }
    }

    public void resetCompleted() {
        this.completeDate = null;
    }

    public static final class Builder {
        private String title;
        private String description;
        private Priority priority;
        private LocalDateTime deadlineDate;
        private final List<Attachment> attachments;

        private Builder() {
            this.attachments = new ArrayList<>();
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder priority(Priority priority) {
            this.priority = priority;
            return this;
        }

        public Builder deadlineDate(LocalDateTime deadlineDate) {
            this.deadlineDate = deadlineDate;
            return this;
        }

        public Builder replaceAttachments(List<Attachment> attachments) {
            this.attachments.clear();
            this.attachments.addAll(attachments);
            return this;
        }

        public Task build() {
            Task task = new Task();
            task.title = title;
            task.description = description;
            task.priority = priority;
            task.deadlineDate = deadlineDate;
            task.attachments.addAll(attachments);
            return task;
        }
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", finished=" + finished +
                ", completeDate=" + completeDate +
                ", deadlineDate=" + deadlineDate +
                ", attachments=" + attachments +
                '}';
    }
}

