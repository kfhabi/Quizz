package quizz.bt.quizz.Repository;
import quizz.bt.quizz.Entity.SchoolClass;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, Integer> {
    
    List<SchoolClass> findByTeacherId(Integer teacherId);
    Optional<SchoolClass> findByAccessCode(String accessCode);
    
}
