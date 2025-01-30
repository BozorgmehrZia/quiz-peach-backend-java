package quiz_peach.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import quiz_peach.domain.dto.CurrentUser;
import quiz_peach.domain.entities.User;
import quiz_peach.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            User user = byEmail.get();
            return new CurrentUser(user, user.getEmail(), user.getPassword(), Collections.emptyList());
        } else {
            throw new UsernameNotFoundException("Can not find user with email " + email);
        }
    }
}
