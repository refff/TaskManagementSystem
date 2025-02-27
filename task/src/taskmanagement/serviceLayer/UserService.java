package taskmanagement.serviceLayer;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import taskmanagement.domain.AppUser;
import taskmanagement.domain.Status;
import taskmanagement.domain.Task;
import taskmanagement.infrastructure.TaskRepository;
import taskmanagement.infrastructure.UserRepository;

import java.util.List;
import java.util.Optional;

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

    private ResponseEntity<?> getAllTasks(){
        List<Task> tasksList = taskRepository.findAllByOrderByIdDesc();
        return new ResponseEntity<>(tasksList, HttpStatus.OK);
    }

    private ResponseEntity<?> getTasksByEmail(String email) {
        List<Task> taskList = taskRepository.findAllByAuthorName(email);
        return new ResponseEntity<>(taskList.reversed(), HttpStatus.OK);
    }

    private ResponseEntity<?> getTasksByAssignee(String assignee) {
        List<Task> taskList = taskRepository.findAllByAssignee(assignee);
        return new ResponseEntity<>(taskList.reversed(), HttpStatus.OK);
    }

    public ResponseEntity<?> getTasksByParameters(String ...params) {
        Optional<String> author = Optional.ofNullable(params[0]);
        Optional<String> assignee = Optional.ofNullable(params[1]);

        if (author.isEmpty() & assignee.isEmpty()) {
            return getAllTasks();
        } else if (author.isEmpty()) {
            return getTasksByAssignee(assignee.get());
        } else if (assignee.isEmpty()) {
            return getTasksByEmail(author.get());
        } else {
            List<Task> taskList = taskRepository.findAllByAssigneeAndAuthorName(assignee.get(), author.get());
            return new ResponseEntity<>(taskList.reversed(), HttpStatus.OK);
        }
    }

    public ResponseEntity<?> createTask(Task task){
        auth = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = userRepository.findByEmail(auth.getName());
        task.setAuthor(user.getEmail());
        task.setUser(user);

        taskRepository.save(task);

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    public ResponseEntity<?> assignTask(String assignee, int taskId) {
        return taskRepository.findById(taskId)
                .map(task -> validateCreator(task)
                        .or(() -> validateEmail(assignee))
                        .orElseGet(() -> updateAssignee(task, assignee)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    public ResponseEntity<?> setStatus(String status, int taskId) {
        return taskRepository.findById(taskId)
                .map(task -> checkPermissions(task)
                        .or(() -> validateStatus(status))
                        .orElseGet(() -> updateStatus(task, status)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    private Optional<ResponseEntity<?>> checkPermissions(Task task) {
        if (!isCreator(task) && !isAssignee(task.getAssignee())) {
            return Optional.of(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
        }
        return Optional.empty();
    }

    private Optional<ResponseEntity<?>> validateCreator(Task task){
        if (!isCreator(task)) {
            return Optional.of(ResponseEntity.status(HttpStatus.FORBIDDEN).build());
        }
        return Optional.empty();
    }

    private Optional<ResponseEntity<?>> validateStatus(String status) {
        try {
            Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Optional.of(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
        }
        return Optional.empty();
    }

    private Optional<ResponseEntity<?>> validateEmail(String assignee) {
        if (!isCorrectEmail(assignee)) {
            return Optional.of(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
        }
        if (!isRegisteredEmail(assignee)) {
            return Optional.of(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        }
        return Optional.empty();
    }

    private ResponseEntity<?> updateStatus(Task task, String status) {
        task.setStatus(status);
        taskRepository.save(task);
        return ResponseEntity.ok(task);
    }

    private ResponseEntity<?> updateAssignee(Task task, String assignee) {
        task.setAssignee(assignee);
        taskRepository.save(task);

        return ResponseEntity.ok(task);
    }

    private boolean isCreator(Task task) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return task.getAuthor().equals(userEmail);
    }

    private boolean isRegisteredEmail(String assignee) {
        List<AppUser> users = userRepository.findAll();
        for (AppUser user : users) {
            if (user.getEmail().equals(assignee) || assignee.equals("none")) {
                return true;
            }
        }
        return false;
    }

    private boolean isCorrectEmail(String email) {
        return email.matches("\\S+@\\S+\\.[a-zA-Z]+") || email.equals("none");
    }

    private boolean isAssignee(String assignee) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        return user.equals(assignee);
    }
}
