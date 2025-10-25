package quizz.bt.quizz.Repository;
import quizz.bt.quizz.Entity.Question;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    
    List<Question> findByCreatorId(Integer creatorIds);
    
}
