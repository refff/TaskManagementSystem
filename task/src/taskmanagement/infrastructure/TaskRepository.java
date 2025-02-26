package taskmanagement.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taskmanagement.domain.Task;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Task findById(int id);
    List<Task> findTasksByAuthorId(int authorId);
    List<Task> findAllByOrderByIdDesc();
    List<Task> findAllByAssigneeAndAuthorName(String assignee, String author);
    List<Task> findAllByAssignee(String assignee);
    List<Task> findAllByAuthorName(String email);
}
