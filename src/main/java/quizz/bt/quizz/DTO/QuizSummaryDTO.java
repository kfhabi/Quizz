package quizz.bt.quizz.DTO;

import lombok.Data;
import quizz.bt.quizz.Entity.Enum.QuizStatus;

@Data
public class QuizSummaryDTO {
    private Integer id;
    private String title;
    private String description;
    private Integer timeLimit;
    private QuizStatus status;
}
