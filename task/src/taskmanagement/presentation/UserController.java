package taskmanagement.presentation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskmanagement.domain.AppUser;
import taskmanagement.domain.Task;
import taskmanagement.serviceLayer.AuthService;
import taskmanagement.serviceLayer.UserService;

import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

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

    @PostMapping(value = "/api/auth/token")
    public ResponseEntity<?> token(HttpServletRequest request) {
        String token = authService.authAndCreateToken(request);
        return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);
    }
}
