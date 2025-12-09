package quizz.bt.quizz.DTO;

import lombok.Data;

@Data
public class RegisterRequest {
    
    private String username;
    private String password;
    private String email;
    private String fullName;
    
}
