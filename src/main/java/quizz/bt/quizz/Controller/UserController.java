package quizz.bt.quizz.Controller;
import quizz.bt.quizz.DTO.RegisterRequest;
import quizz.bt.quizz.Entity.User;
import quizz.bt.quizz.Service.UserService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerStudent(@RequestBody RegisterRequest request) {

        try {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            user.setEmail(request.getEmail());
            user.setFullName(request.getFullName());

            User newUser = userService.registerUser(user);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // Da ton tai
        }
    }

    // Admin: create teacher
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/teachers")
    public ResponseEntity<?> createTeacher(@RequestBody User user) {
        User created = userService.registerUser(user);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody User updated) {
        try {
            User u = userService.updateUser(id, updated);
            return ResponseEntity.ok(u);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(user); // Tra ve thanh cong
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
}
