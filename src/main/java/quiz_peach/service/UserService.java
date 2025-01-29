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
        if (userRequestDTO.name() == null || userRequestDTO.email() == null || userRequestDTO.password() == null) {
            throw new RuntimeException("Name, email, and password are required.");
        }

        if (userRepository.existsByNameOrEmail(userRequestDTO.name(), userRequestDTO.email())) {
            throw new RuntimeException("User with this name or email already exists.");
        }

        User user = new User();
        user.setName(userRequestDTO.name());
        user.setEmail(userRequestDTO.email());
        user.setPassword(passwordEncoder.encode(userRequestDTO.password()));
        userRepository.save(user);

        return new UserResponseDTO(user.getId(), user.getName(), user.getScore());
    }

    public void loginUser(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.email());
        if (user == null || !passwordEncoder.matches(loginRequestDTO.password(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password.");
        }
    }

    public void logout() {

    }
}
