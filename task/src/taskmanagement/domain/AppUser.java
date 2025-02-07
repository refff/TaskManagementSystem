package taskmanagement.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
@Table(name = "users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty
    private String email;
    @NotEmpty
    @NotBlank
    @Size(min = 6)
    private String password;
    private String authority;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Task> tasks;

    public AppUser() {
    }

    @JsonCreator
    public AppUser(@JsonProperty("email") String email,
                @JsonProperty("password") String password) {
        this.email = email.toLowerCase();
        this.password = password;
        this.authority = "ROLE_USER";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
