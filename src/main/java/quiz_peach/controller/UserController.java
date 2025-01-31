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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(userService.registerUser(userRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO loginResponseDTO = userService.loginUser(loginRequestDTO);
        String username = URLEncoder.encode(loginResponseDTO.name(), StandardCharsets.UTF_8);
        String cookie = "username=%s; Path=/; Http-Only=false; Secure=false; SameSite=Lax;".formatted(username);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie)
                .body(loginResponseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        String cookie = "username=; Path=/; Http-Only=false; Secure=false; SameSite=Lax; Max-Age=0";
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie).body("Logout successful");
    }

    @PostMapping("/follow/{username}")
    public ResponseEntity<String> followUser(@PathVariable String username,
                                             @AuthenticationPrincipal CurrentUser user) {
        userService.followUser(username, user);
        return ResponseEntity.ok("Successfully followed " + username);
    }

    @PostMapping("/unfollow/{username}")
    public ResponseEntity<String> unfollowUser(@PathVariable String username,
                                               @AuthenticationPrincipal CurrentUser user) {
        userService.unfollowUser(username, user);
        return ResponseEntity.ok("Successfully unfollowed " + username);
    }
}