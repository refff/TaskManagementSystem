package taskmanagement.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private int id;
    @NotBlank
    @NotNull
    private String title;
    @NotBlank
    @NotNull
    private String description;
    private String status = "CREATED";
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private AppUser author;
    private String authorName;
    private String assignee = "none";
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Comment> comments;

    public Task(){
    }

    public Task(String title, String description, AppUser user) {
        this.title = title;
        this.description = description;
        this.author = user;
        this.authorName = user.getEmail();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AppUser getUser() {
        return author;
    }

    @JsonIgnore
    public void setUser(AppUser user) {
        this.author = user;
    }

    public String getAuthor() {
        return authorName;
    }

    public void setAuthor(String authorName) {
        this.authorName = authorName;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public void setAuthor(AppUser author) {
        this.author = author;
    }

    @JsonProperty(value = "total_comments", required = false)
    public int getTotalComments() {
        return comments.size();
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
