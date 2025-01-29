package quiz_peach.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quiz_peach.domain.dto.LoginRequestDTO;
import quiz_peach.domain.dto.UserRequestDTO;
import quiz_peach.domain.dto.UserResponseDTO;
import quiz_peach.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers(@RequestParam(required = false) String name,
                                                          @RequestParam(defaultValue = "desc") String sort) {
        return ResponseEntity.ok(userService.getUsers(name, sort));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(userService.registerUser(userRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        userService.loginUser(loginRequestDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logoutUser() {
        userService.logout();
        return ResponseEntity.ok().build();
    }

}