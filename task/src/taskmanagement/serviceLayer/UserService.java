package taskmanagement.serviceLayer;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import taskmanagement.domain.AppUser;
import taskmanagement.domain.Task;
import taskmanagement.infrastructure.TaskRepository;
import taskmanagement.infrastructure.UserRepository;

import java.util.List;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TaskRepository taskRepository;
    private Authentication auth;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.taskRepository = taskRepository;
    }

    public ResponseEntity<?> createUser(AppUser request){
        if (userRepository.findUserByEmail(request.getEmail()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        AppUser user = new AppUser(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> getAllTasks(){
        List<Task> tasksList = taskRepository.findAllByOrderByIdDesc();

        return new ResponseEntity<>(tasksList, HttpStatus.OK);
    }

    public ResponseEntity<?> getTasksByEmail(String email) {
        AppUser author = userRepository.findByEmail(email);
        if(author == null) return new ResponseEntity<>(List.of(), HttpStatus.OK);
        int authorId = author.getId();
        List<Task> tasksList =  taskRepository.findTasksByAuthorId(authorId);

        return new ResponseEntity<>(tasksList.reversed(), HttpStatus.OK);
    }


    public ResponseEntity<?> createTask(Task task){
        auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = userRepository.findByEmail(auth.getName());
        task.setAuthor(user.getEmail());
        task.setUser(user);

        taskRepository.save(task);

        return new ResponseEntity<>(task, HttpStatus.OK);
    }
}
