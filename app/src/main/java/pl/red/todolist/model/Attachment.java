package pl.red.todolist.model;

import java.io.Serializable;
import java.util.Objects;

public class Attachment implements Serializable {
    private String path;

    public Attachment(String name) {
        this.path = name;
    }

    public String getName() {
        return path;
    }


    public void setName(String name) {
        this.path = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attachment that = (Attachment) o;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
