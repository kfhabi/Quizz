package quizz.bt.quizz.Service;
import quizz.bt.quizz.Entity.User;
import quizz.bt.quizz.Entity.Enum.Role;
import quizz.bt.quizz.Repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    //private final  PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        //this.passwordEncoder = passwordEncoder;
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(" " + id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(" " + username));
    }

    public User registerUser(User user) {
        
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("");
        }

        //String hashedPassword = passwordEncoder.encode(user.getPassword());
        //user.setPassword(hashedPassword);
        user.setPassword(user.getPassword());

        user.setRole(Role.STUDENT);

        return userRepository.save(user);
    }

    public List<User> getAllTeachers() {
        return userRepository.findByRole(Role.TEACHER);
    }
}
