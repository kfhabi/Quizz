package quizz.bt.quizz.DTO;

import lombok.Data;
import quizz.bt.quizz.Entity.Enum.QuestionType;
import java.util.List;

@Data
public class QuestionDTO {
    private Integer id;
    private String questionText;
    private QuestionType questionType;
    private String explanation;
    private List<AnswerDTO> answers;
}
