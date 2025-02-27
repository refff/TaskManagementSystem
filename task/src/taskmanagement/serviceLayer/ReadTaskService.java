package taskmanagement.serviceLayer;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import taskmanagement.domain.Task;
import taskmanagement.infrastructure.TaskRepository;
import taskmanagement.infrastructure.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReadTaskService extends TaskService {

    public ReadTaskService(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           TaskRepository taskRepository) {
        super(userRepository, passwordEncoder, taskRepository);
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
}
