package quizz.bt.quizz.Repository;
import quizz.bt.quizz.Entity.QuizAssignment;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface QuizAssignmentRepository extends JpaRepository<QuizAssignment, Integer> {
    
    List<QuizAssignment> findByQuizId(Integer quizId);
    List<QuizAssignment> findBySchoolClassId(Integer classId);
}