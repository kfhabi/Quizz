package quizz.bt.quizz.DTO;

import lombok.Data;

@Data
public class CreateClassRequest {
    
    private String className;
    private Integer teacherId; // sau nay lay tu dang nhap
}
