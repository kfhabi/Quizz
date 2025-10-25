package quizz.bt.quizz.Entity;
import quizz.bt.quizz.Entity.Enum.QuizStatus;
import quizz.bt.quizz.Entity.Enrollment.QuizQuestion;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Set;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quizzes")
public class Quiz {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id", nullable = false)
    private Integer id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    @Column(name = "time_limit")
    private Integer timeLimit; 

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private QuizStatus status;

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    // Quan hệ
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @OneToMany(mappedBy = "quiz")
    private Set<QuizQuestion> quizzQuestions;

    @OneToMany(mappedBy = "quiz")
    private Set<QuizAssignment> assignments;

    @OneToMany(mappedBy = "quiz")
    private Set<Result> results;

}
