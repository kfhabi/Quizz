package quizz.bt.quizz.DTO;

import lombok.Data;
import java.time.Instant;

@Data
public class AssignQuizRequest {
    
    private Integer quizId;
    private Integer classId;
    private Integer teacherId; // sau nay lay tu dang nhap
    private Instant dueDate;
}
