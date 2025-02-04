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
    public ResponseEntity<?> getTasks(){
        //System.out.println(email);
        return userService.getTasks();
    }

    @PostMapping(value = "/api/tasks")
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskDto task) {
        return userService.createTask(task);
    }

    /*@GetMapping(value = "/api/users")
    public ResponseEntity<?> getUser(@RequestBody AppUser user){
        return userService.findUser(user.getEmail());
    }*/
}
