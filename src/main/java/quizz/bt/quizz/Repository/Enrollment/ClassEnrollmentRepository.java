package quizz.bt.quizz.Repository.Enrollment;
import quizz.bt.quizz.Entity.Enrollment.ClassEnrollment;
import quizz.bt.quizz.Entity.Enrollment.ClassEnrollmentId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ClassEnrollmentRepository extends JpaRepository<ClassEnrollment, ClassEnrollmentId> {
    
    List<ClassEnrollment> findByStudentId(Integer studentId);
    List<ClassEnrollment> findByIdClassId(Integer classId);
    @Query("SELECT ce FROM ClassEnrollment ce WHERE ce.id.classId = :classId")
    List<ClassEnrollment> findByClassIdExplicit(@Param("classId") Integer classId);
    
}
