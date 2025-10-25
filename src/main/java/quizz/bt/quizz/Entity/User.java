package quizz.bt.quizz.Entity;
import quizz.bt.quizz.Entity.Enum.Role;
import quizz.bt.quizz.Entity.Enrollment.ClassEnrollment;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Set;

import javax.naming.spi.DirStateFactory.Result;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "password", nullable = false, length = 50)
    private String password;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    // Quan hệ
    @OneToMany(mappedBy = "teacher")
    private Set<SchoolClass> classesTaught;

    @OneToMany(mappedBy = "creator")
    private Set<Quiz> createdQuizzes;

    @OneToMany(mappedBy = "creator")
    private Set<Question> createdQuestions;

    @OneToMany(mappedBy = "assigner")
    private Set<QuizAssignment> assignmentsGiven;

    @OneToMany(mappedBy = "user")
    private Set<Result> results;

    @OneToMany (mappedBy = "student")
    private Set<ClassEnrollment> enrollments;

}

