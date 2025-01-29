package quiz_peach.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import quiz_peach.domain.dto.LoginRequestDTO;
import quiz_peach.domain.dto.UserRequestDTO;
import quiz_peach.domain.dto.UserResponseDTO;
import quiz_peach.domain.entities.User;
import quiz_peach.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponseDTO> getUsers(String name, String sort) {
        List<User> users = userRepository.findByNameContainingIgnoreCaseOrderByScore(name, sort.equalsIgnoreCase("desc"));
        return users.stream()
                    .map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getScore()))
                    .collect(Collectors.toList());
    }

    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByNameOrEmail(userRequestDTO.name(), userRequestDTO.email())) {
            throw new RuntimeException("User with this name or email already exists.");
        }

        User user = User.builder()
                        .name(userRequestDTO.name())
                        .email(userRequestDTO.email())
                        .password(passwordEncoder.encode(userRequestDTO.password()))
                        .build();
        final User createdUser = userRepository.save(user);

        return new UserResponseDTO(createdUser.getId(), createdUser.getName(), createdUser.getScore());
    }

    public void loginUser(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.email());
        if (user == null || !passwordEncoder.matches(loginRequestDTO.password(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password.");
        }
    }

    public void logout(final User user) {

    }
}
