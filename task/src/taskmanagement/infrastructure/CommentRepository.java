package taskmanagement.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import taskmanagement.domain.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByTaskId(int taskId);
}
