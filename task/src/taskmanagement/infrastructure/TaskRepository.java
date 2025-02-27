package taskmanagement.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taskmanagement.domain.Task;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<Task> findById(int id);
    List<Task> findAllByOrderByIdDesc();
    List<Task> findAllByAssigneeAndAuthorName(String assignee, String author);
    List<Task> findAllByAssignee(String assignee);
    List<Task> findAllByAuthorName(String email);
}
