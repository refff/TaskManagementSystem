package taskmanagement.presentation;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskmanagement.domain.AppUser;
import taskmanagement.domain.Task;
import taskmanagement.serviceLayer.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/api/accounts")
    public ResponseEntity<?> registration(@Valid @RequestBody AppUser user){
        return userService.createUser(user);
    }

    @GetMapping(value = "/api/tasks")
    public ResponseEntity<?> getTasks(@RequestParam(name = "author", required = false) String email){
        return email == null ? userService.getAllTasks() : userService.getTasksByEmail(email);
    }

    @PostMapping(value = "/api/tasks")
    public ResponseEntity<?> createTask(@Valid @RequestBody Task task) {
        return userService.createTask(task);
    }
}
