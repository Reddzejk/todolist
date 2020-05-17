package pl.red.todolist.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Task {
    private final UUID id;
    private String title;
    private String description;
    private Priority priority;
    private boolean finished;
    private LocalDateTime creationDate;
    private LocalDateTime completeDate;
    private LocalDateTime deadlineDate;
    private final Map<UUID, Attachment> attachments;

    private Task() {
        this.id = UUID.randomUUID();
        finished = false;
        completeDate = null;
        this.creationDate = LocalDateTime.now();
        this.attachments = new HashMap<>();
    }

    public static Builder builder() {
        return new Builder();
    }

    public UUID getId() {
        return id;
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

    public boolean isFinished() {
        return finished;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getCompleteDate() {
        return completeDate;
    }

    public LocalDateTime getDeadlineDate() {
        return deadlineDate;
    }

    public Map<UUID, Attachment> getAttachments() {
        return attachments;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setCompleteDate(LocalDateTime completeDate) {
        this.completeDate = completeDate;
    }

    public void setDeadlineDate(LocalDateTime deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public static final class Builder {
        private String title;
        private String description;
        private Priority priority;
        private LocalDateTime deadlineDate;
        private final Map<UUID, Attachment> attachments;

        private Builder() {
            this.attachments = new HashMap<>();
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

        public Builder addAttachment(Attachment attachment) {
            this.attachments.put(attachment.getId(), attachment);
            return this;
        }

        public Task build() {
            Task task = new Task();
            task.title = title;
            task.description = description;
            task.priority = priority;
            task.deadlineDate = deadlineDate;
            task.attachments.putAll(attachments);
            return task;
        }
    }

}

