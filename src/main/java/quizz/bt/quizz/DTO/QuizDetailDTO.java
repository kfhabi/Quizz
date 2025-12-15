package quizz.bt.quizz.DTO;

import lombok.Data;
import quizz.bt.quizz.Entity.Enum.QuizStatus;
import java.util.List;

@Data
public class QuizDetailDTO {
    private Integer id;
    private String title;
    private String description;
    private Integer timeLimit;
    private QuizStatus status;
    private List<QuestionDTO> questions;
}
