package quiz_peach.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import quiz_peach.domain.dto.*;
import quiz_peach.domain.entities.User;
import quiz_peach.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public List<UserResponseDTO> getUsers(String name, String sort, CurrentUser currentUser) {
        Long currentUserId = currentUser.getUser().getId();
        List<User> otherUsers = userRepository.findByNameContainingIgnoreCaseAndIdNotOrderByScore(name, currentUserId, sort.equalsIgnoreCase("desc"));

        List<User> allUsers = new ArrayList<>();
        allUsers.add(currentUser.getUser());
        allUsers.addAll(otherUsers);

        int rank = 1;
        List<UserResponseDTO> result = new ArrayList<>();

        for (int i = 0; i < allUsers.size(); i++) {
            User user = allUsers.get(i);
            if (i > 0 && !user.getScore().equals(allUsers.get(i - 1).getScore())) {
                rank = i + 1;
            }
            result.add(new UserResponseDTO(user.getId(), user.getName(), user.getScore(), rank));
        }

        return result;
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
        User createdUser = userRepository.save(user);
        return new UserResponseDTO(createdUser.getId(), createdUser.getName(), createdUser.getScore(), null);
    }

    public LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO) {
        String email = loginRequestDTO.email();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Incorrect email or password"));
        if (!passwordEncoder.matches(loginRequestDTO.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect email or password");
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, loginRequestDTO.password()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return new LoginResponseDTO(user.getName(), "Bearer " + jwtTokenService.generateToken(email));
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login failed: " + e.getMessage());
        }
    }
}
