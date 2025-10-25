package quizz.bt.quizz.Entity;
import quizz.bt.quizz.Entity.Enrollment.ClassEnrollment;

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
@Table(name = "classes")
public class SchoolClass {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Integer id;

    @Column(name = "class_name", nullable = false, length = 100)
    private String className;

    @Column(name = "access_code", nullable = false, length = 10)
    private String accessCode;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();

    // Quan hệ
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @OneToMany(mappedBy = "schoolClass")
    private Set<ClassEnrollment> enrollments;

    @OneToMany(mappedBy = "schoolClass")
    private Set<QuizAssignment> assignments;
}
