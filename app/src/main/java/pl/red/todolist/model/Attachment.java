package pl.red.todolist.model;

import java.util.UUID;

class Attachment {
    private final UUID id;
    private String name;
    private String content;

    public Attachment(String name, String content) {
        id = UUID.randomUUID();
        this.name = name;
        this.content = content;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
