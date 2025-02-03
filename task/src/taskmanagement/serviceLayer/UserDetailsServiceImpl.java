package taskmanagement.serviceLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import taskmanagement.domain.*;
import taskmanagement.infrastructure.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = userRepository
                .findUserByEmail(email.toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("Not found!"));

        return new AppUserAdapter(user);
    }
}
