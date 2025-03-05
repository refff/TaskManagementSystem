package taskmanagement.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jdk.jfr.Enabled;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(value = "id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private int id;
    @NotBlank
    @NotNull
    private String text;
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
    private String author;

    public Comment() {
    }

    public Comment(String text, Task task) {
        this.text = text;
        this.task = task;
    }

    public int getId() {
        return id;
    }

    public void setId(int commentId) {
        this.id = commentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("task_id")
    public String getTask() {
        return task != null? String.valueOf(task.getId()):null;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


}
