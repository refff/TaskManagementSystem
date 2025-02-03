package taskmanagement.serviceLayer;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import taskmanagement.domain.AppUserDetails;

@Service
public interface UserDetailsService {
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
}
