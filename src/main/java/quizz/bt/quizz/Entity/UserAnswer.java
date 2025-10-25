package quizz.bt.quizz.Entity;

import jakarta.persistence.*;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
@Entity
@Table(name = "user_answers")
public class UserAnswer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_answer_id", nullable = false)
    private Integer id;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    // Quan hệ
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "result_id", nullable = false)
    private Result result;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_answer_id")
    private Answer selectedAnswer;
}
