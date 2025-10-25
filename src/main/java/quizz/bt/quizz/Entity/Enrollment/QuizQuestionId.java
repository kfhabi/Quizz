package quizz.bt.quizz.Entity.Enrollment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class QuizQuestionId implements Serializable {
    
    @Column(name = "quiz_id")
    private Integer quizId;

    @Column(name = "question_id")
    private Integer questionId;

    public QuizQuestionId() {}

    public QuizQuestionId(Integer quizId, Integer questionId) {
        this.quizId = quizId;
        this.questionId = questionId;
    }

        @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuizQuestionId that = (QuizQuestionId) o;
        return Objects.equals(quizId, that.quizId) &&
               Objects.equals(questionId, that.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quizId, questionId);
    }

}
