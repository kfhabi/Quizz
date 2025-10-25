package quizz.bt.quizz.Entity.Enrollment;

import jakarta.persistence.*;
import quizz.bt.quizz.Entity.Quiz;
import quizz.bt.quizz.Entity.Question;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quiz_questions")
public class QuizQuestion {
    
    @EmbeddedId // Sử dụng Khóa chính tổng hợp
    private QuizQuestionId id;

    @Column(name = "question_order")
    private Integer questionOrder;

    // Quan hệ
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("quizId")
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("questionId")
    @JoinColumn(name = "question_id")
    private Question question;
}
