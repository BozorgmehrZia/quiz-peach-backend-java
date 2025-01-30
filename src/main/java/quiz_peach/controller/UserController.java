package quiz_peach.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import quiz_peach.domain.dto.*;
import quiz_peach.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers(@RequestParam(required = false) String name,
                                                          @RequestParam(defaultValue = "desc") String sort,
                                                          @AuthenticationPrincipal CurrentUser user) {
        return ResponseEntity.ok(userService.getUsers(name, sort, user));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(userService.registerUser(userRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO loginResponseDTO = userService.loginUser(loginRequestDTO);
        String cookie = "username=%s; HttpOnly=false; Secure=false; SameSite=Lax;".formatted(loginResponseDTO.name());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, loginResponseDTO.token())
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION)
                .header(HttpHeaders.SET_COOKIE, cookie)
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        String cookie = "username=; HttpOnly=false; Secure=false; SameSite=Lax;";
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie).body("Logout successful");
    }

}