package quizz.bt.quizz.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import quizz.bt.quizz.Security.JwtUtil;
import quizz.bt.quizz.Repository.UserRepository;
import quizz.bt.quizz.Entity.User;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (userRepository.findByUsername(username).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username exists"));
        }
        User u = new User();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(password));
        u.setEmail(body.getOrDefault("email", username + "@example.com"));
        u.setFullName(body.getOrDefault("fullName", username));
        u.setRole(null);
        userRepository.save(u);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        return userRepository.findByUsername(username)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .map(u -> ResponseEntity.ok(Map.of("token", jwtUtil.generateToken(u.getUsername()))))
                .orElseGet(() -> ResponseEntity.status(401).body(Map.of("error", "Invalid")));
    }
}
