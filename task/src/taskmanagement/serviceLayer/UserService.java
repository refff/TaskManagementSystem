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
import taskmanagement.presentation.TaskDto;

import java.util.List;

@Service
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

    @Transactional
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

    @Transactional
    public ResponseEntity<?> getTasks(){
        auth = SecurityContextHolder.getContext().getAuthentication();
        int authorId = userRepository.findByEmail(auth.getName()).getId();
        List<Task> tasksList =  taskRepository.findTasksByAuthorId(authorId);

        return new ResponseEntity<>(tasksList, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> createTask(TaskDto taskDto){
        auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = userRepository.findByEmail(auth.getName());
        Task task = new Task(taskDto.getTitle(), taskDto.getDescription(), user);

        //task.setAuthor(auth.getName());
        
        taskRepository.save(task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    /*public ResponseEntity<?> findUser(String email){
        AppUser user = userRepository.findByEmail(email);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }*/
}
