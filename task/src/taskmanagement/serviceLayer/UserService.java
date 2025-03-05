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

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

}
