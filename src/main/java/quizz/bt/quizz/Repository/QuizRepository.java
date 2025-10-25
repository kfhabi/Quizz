package quizz.bt.quizz.Repository;
import quizz.bt.quizz.Entity.Quiz;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Integer> {
    
    List<Quiz> findByCreatorId(Integer creatorId);
    
}
