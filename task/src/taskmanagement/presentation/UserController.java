package taskmanagement.presentation;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import taskmanagement.domain.AppUser;
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
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    /*@GetMapping(value = "/api/users")
    public ResponseEntity<?> getUser(@RequestBody AppUser user){
        return userService.findUser(user.getEmail());
    }*/
}
