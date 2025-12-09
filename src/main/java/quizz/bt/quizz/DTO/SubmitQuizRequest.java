package quizz.bt.quizz.DTO;

import lombok.Data;
import java.util.Map;

@Data
public class SubmitQuizRequest {
    
    private Integer studentId; // sau nay lay tu dang nhap
    private Map<Integer, Integer> answers; // key: QuestionId, value: SelectedAnswerID    
}
