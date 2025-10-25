package quizz.bt.quizz.Repository;
import quizz.bt.quizz.Entity.Answer;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    
    List<Answer> findByQuestionId(Integer questionId);

    Set<Answer> findByQuestionIdInAndIsCorrect(Collection<Integer> questionIds, Boolean isCorrect);
}
