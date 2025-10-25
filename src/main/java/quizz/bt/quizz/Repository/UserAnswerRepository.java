package quizz.bt.quizz.Repository;
import quizz.bt.quizz.Entity.UserAnswer;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Integer> {
    
    List<UserAnswer> findByResultId(Integer resultId);
    
}
