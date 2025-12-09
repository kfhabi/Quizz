package quizz.bt.quizz.DTO;

import lombok.Data;

@Data
public class JoinClassRequest {
    
    private String accessCode;
    private Integer studentId; // sau nay lay tu dang nhap
}
