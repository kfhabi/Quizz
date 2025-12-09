package quizz.bt.quizz.Security;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import quizz.bt.quizz.Entity.User;
import quizz.bt.quizz.Entity.Enum.Role;
import quizz.bt.quizz.Repository.UserRepository;

@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            admin.setFullName("Administrator");
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        }
    }
}
