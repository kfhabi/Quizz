package quizz.bt.quizz.Repository.Enrollment;
import quizz.bt.quizz.Entity.Enrollment.QuizQuestion;
import quizz.bt.quizz.Entity.Enrollment.QuizQuestionId;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, QuizQuestionId> {
    
    List<QuizQuestion> findByQuizId(Integer quizId);
    List<QuizQuestion> findByQuestionId(Integer questionId);
    
}
