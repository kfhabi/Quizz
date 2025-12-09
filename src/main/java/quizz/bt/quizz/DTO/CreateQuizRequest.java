package quizz.bt.quizz.DTO;
import quizz.bt.quizz.Entity.Enum.QuizStatus;

import lombok.Data;
import java.util.List;

@Data
public class CreateQuizRequest {
    
    // Thong tin quiz
    private String title;
    private String description;
    private Integer timeLimit;
    private QuizStatus status = QuizStatus.DRAFT;

    // Thong tin nguoi tao
    private Integer creatorId; // sau nay lay tu dang nhap

    // Danh sach cau hoi
    private List<Integer> questionIds;
}
