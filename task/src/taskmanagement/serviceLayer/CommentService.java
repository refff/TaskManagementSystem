package taskmanagement.serviceLayer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taskmanagement.domain.Comment;
import taskmanagement.domain.Task;
import taskmanagement.infrastructure.CommentRepository;
import taskmanagement.infrastructure.TaskRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
    }

    public ResponseEntity<?> postComment(String text, int taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        Comment comment = new Comment(text, task.get());
        String author = SecurityContextHolder.getContext().getAuthentication().getName();
        comment.setAuthor(author);

        commentRepository.save(comment);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> getAllComments(int taskId) {
        List<Comment> list = commentRepository.findAllByTaskId(taskId);

        if (taskRepository.findById(taskId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(list.reversed(), HttpStatus.OK);
    }
}
