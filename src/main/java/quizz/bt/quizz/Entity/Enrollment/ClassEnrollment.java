package quizz.bt.quizz.Entity.Enrollment;
import quizz.bt.quizz.Entity.SchoolClass;
import quizz.bt.quizz.Entity.User;

import jakarta.persistence.*;
import java.time.Instant;

import lombok.Setter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "class_enrollments")
public class ClassEnrollment {
    
    @EmbeddedId // Sử dụng Khóa chính tổng hợp
    private ClassEnrollmentId id;

    @Column(name = "joined_at", updatable = false)
    private Instant joinedAt = Instant.now();

    // Quan he
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("classId") 
    @JoinColumn(name = "class_id")
    private SchoolClass schoolClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("studentId") 
    @JoinColumn(name = "student_id")
    private User student;
}
