package taskmanagement.serviceLayer.TaskService;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import taskmanagement.infrastructure.TaskRepository;
import taskmanagement.infrastructure.UserRepository;

@Service
public class TaskService {
    protected final UserRepository userRepository;
    protected final PasswordEncoder passwordEncoder;
    protected final TaskRepository taskRepository;
    protected Authentication auth;

    public TaskService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.taskRepository = taskRepository;
    }
}
