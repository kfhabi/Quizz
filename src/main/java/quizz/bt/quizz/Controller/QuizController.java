package quizz.bt.quizz.Controller;

import quizz.bt.quizz.DTO.*;
import quizz.bt.quizz.Service.QuizService;
import quizz.bt.quizz.Entity.*;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Nhớ import dòng này
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/quizzes")
public class QuizController {
    
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    // ==================================================================
    // 1. TẠO ĐỀ THI
    // SỬA: Cho phép cả STUDENT, TEACHER, ADMIN đều được tạo
    // ==================================================================
    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER', 'ADMIN', 'STUDENT')") 
    public ResponseEntity<?> createQuiz(@RequestBody CreateQuizRequest request) {
        try {
            Quiz quiz = new Quiz();
            quiz.setTitle(request.getTitle());
            quiz.setDescription(request.getDescription());
            quiz.setTimeLimit(request.getTimeLimit());
            quiz.setStatus(request.getStatus());

            // Lưu ý: Logic service cần đảm bảo creatorId khớp với user đang đăng nhập (nếu cần bảo mật cao hơn)
            Quiz newQuiz = quizService.createQuiz(quiz, request.getCreatorId(), request.getQuestionIds());
            return new ResponseEntity<>(newQuiz, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    // ==================================================================
    // 2. NỘP BÀI THI
    // Cho phép tất cả Role đều được nộp bài
    // ==================================================================
    @PostMapping("/{quizId}/submit")
    @PreAuthorize("hasAnyRole('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<?> submitQuiz(@PathVariable Integer quizId, @RequestBody SubmitQuizRequest request) {
        try {
            Result result = quizService.submitQuiz(quizId, request.getStudentId(), request.getAnswers());
            return ResponseEntity.ok(result); 
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}