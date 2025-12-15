package quizz.bt.quizz.Service;
import quizz.bt.quizz.Entity.User;
import quizz.bt.quizz.Entity.Enum.Role;
import quizz.bt.quizz.Repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
    }

    // --- ĐÂY LÀ HÀM CẦN SỬA ---
    public User registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username exists");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email exists");
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // <--- SỬA Ở ĐÂY: Logic thông minh hơn
        // Nếu Controller chưa gán Role (nghĩa là null), thì mới mặc định là STUDENT.
        // Còn nếu Controller đã gán là TEACHER rồi thì giữ nguyên.
        if (user.getRole() == null) {
            user.setRole(Role.STUDENT);
        }
        // -------------------------

       return userRepository.save(user);
    }

    // Admin operations
    public User createTeacher(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.TEACHER);
        return userRepository.save(user);
    }

    public User updateUser(Integer id, User updated) {
        User u = getUserById(id);
        u.setEmail(updated.getEmail());
        u.setFullName(updated.getFullName());
        if (updated.getPassword() != null && !updated.getPassword().isBlank()) {
            u.setPassword(passwordEncoder.encode(updated.getPassword()));
        }
        if (updated.getRole() != null) u.setRole(updated.getRole());
        return userRepository.save(u);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public List<User> getAllTeachers() {
        return userRepository.findByRole(Role.TEACHER);
    }

    public User login(String username, String password)
    {
        User user = getUserByUsername(username);

        if (!passwordEncoder.matches(password, user.getPassword()))
        {
           throw new IllegalArgumentException("Wrong password");
        }
        return user;
    }
}