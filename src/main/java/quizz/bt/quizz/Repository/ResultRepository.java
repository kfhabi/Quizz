package quizz.bt.quizz.Repository;
import quizz.bt.quizz.Entity.Result;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Integer> {
    
    List<Result> findByUserId(Integer userId);
    List<Result> findByQuizId(Integer quizId);
    List<Result> findByUserIdAndQuizId(Integer userId, Integer quizId);
}
