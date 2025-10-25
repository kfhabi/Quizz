package quizz.bt.quizz.Repository.Enrollment;
import quizz.bt.quizz.Entity.Enrollment.ClassEnrollment;
import quizz.bt.quizz.Entity.Enrollment.ClassEnrollmentId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassEnrollmentRepository extends JpaRepository<ClassEnrollment, ClassEnrollmentId> {
    
    List<ClassEnrollment> findByStudentId(Integer studentId);
    List<ClassEnrollment> findByClassId(Integer classId);
    
}
