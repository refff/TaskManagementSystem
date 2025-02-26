package taskmanagement.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import taskmanagement.domain.AppUser;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer> {
    Optional<AppUser> findUserByEmail(String email);
    AppUser findByEmail(String email);
    int findIdByEmail(String email);
}
