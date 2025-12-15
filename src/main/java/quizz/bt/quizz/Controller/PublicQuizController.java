package quizz.bt.quizz.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import quizz.bt.quizz.DTO.*;
import quizz.bt.quizz.Entity.*;
import quizz.bt.quizz.Repository.QuizRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/public")
@CrossOrigin(origins = "*")
public class PublicQuizController {

    private final QuizRepository quizRepository;

    public PublicQuizController(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @GetMapping("/quizzes")
    public ResponseEntity<List<QuizSummaryDTO>> listQuizzes() {
        List<QuizSummaryDTO> list = quizRepository.findAll()
                .stream()
                .map(q -> {
                    QuizSummaryDTO dto = new QuizSummaryDTO();
                    dto.setId(q.getId());
                    dto.setTitle(q.getTitle());
                    dto.setDescription(q.getDescription());
                    dto.setTimeLimit(q.getTimeLimit());
                    dto.setStatus(q.getStatus());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/quizzes/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<QuizDetailDTO> getQuiz(@PathVariable Integer id) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

        QuizDetailDTO dto = new QuizDetailDTO();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setDescription(quiz.getDescription());
        dto.setTimeLimit(quiz.getTimeLimit());
        dto.setStatus(quiz.getStatus());

        List<QuestionDTO> questions = quiz.getQuizzQuestions()
                .stream()
                .sorted(Comparator.comparingInt(qr -> qr.getQuestionOrder() == null ? 0 : qr.getQuestionOrder()))
                .map(qr -> {
                    Question question = qr.getQuestion();
                    QuestionDTO qdto = new QuestionDTO();
                    qdto.setId(question.getId());
                    qdto.setQuestionText(question.getQuestionText());
                    qdto.setQuestionType(question.getQuestionType());
                    qdto.setExplanation(question.getExplanation());

                    List<AnswerDTO> answers = question.getAnswers()
                            .stream()
                            .map(a -> {
                                AnswerDTO ad = new AnswerDTO();
                                ad.setId(a.getId());
                                ad.setAnswerText(a.getAnswerText());
                                return ad;
                            })
                            .collect(Collectors.toList());

                    qdto.setAnswers(answers);
                    return qdto;
                })
                .collect(Collectors.toList());

        dto.setQuestions(questions);

        return ResponseEntity.ok(dto);
    }
}
