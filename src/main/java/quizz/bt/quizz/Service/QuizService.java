package quizz.bt.quizz.Service;
import quizz.bt.quizz.Entity.*;
import quizz.bt.quizz.Entity.Enrollment.*;
import quizz.bt.quizz.Repository.Enrollment.*;
import quizz.bt.quizz.Repository.*;

import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QuizService {
    
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final ResultRepository resultRepository;
    private final UserRepository userRepository;
    private final UserAnswerRepository userAnswerRepository;

    public QuizService(QuizRepository quizRepository,
                       QuestionRepository questionRepository,
                       AnswerRepository answerRepository,
                       QuizQuestionRepository quizQuestionRepository,
                       ResultRepository resultRepository,
                       UserRepository userRepository,
                       UserAnswerRepository userAnswerRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.quizQuestionRepository = quizQuestionRepository;
        this.resultRepository = resultRepository;
        this.userRepository = userRepository;
        this.userAnswerRepository = userAnswerRepository;
    }

    @Transactional
    public Quiz createQuiz(Quiz quiz, Integer creatorId, List<Integer> questionIds) {

        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new EntityNotFoundException(""));
        quiz.setCreator(creator);

        Quiz savedQuiz = quizRepository.save(quiz);

        List<Question> questions = questionRepository.findAllById(questionIds);
        if (questions.size() != questionIds.size()) {
            throw new IllegalArgumentException("");
        }

        int order = 1;
        for (Question question : questions) {
            QuizQuestionId quizQuestionId = new QuizQuestionId(savedQuiz.getId(), question.getId());
            QuizQuestion quizQuestion = new QuizQuestion();
            quizQuestion.setId(quizQuestionId);
            quizQuestion.setQuiz(savedQuiz);
            quizQuestion.setQuestion(question);
            quizQuestion.setQuestionOrder(order++);

            quizQuestionRepository.save(quizQuestion);
        }
        return savedQuiz;
    }

    public Quiz getQuizForTaking(Integer quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException(" "));
    }

    @Transactional
    public Result submitQuiz(Integer quizId, Integer studentId, Map<Integer, Integer> answers) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException(" "));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException(" "));

        Result result = new Result();
        result.setQuiz(quiz);
        result.setUser(student);
        result.setStartedAt(Instant.now());

        Result savedResult = resultRepository.save(result);

        int correctCount = 0;

        Set<Integer> correctAnswers = answerRepository.findByQuestionIdInAndIsCorrect(answers.keySet(), true)
                .stream()
                .map(Answer::getId)
                .collect(Collectors.toSet());

        for (Map.Entry<Integer, Integer> entry : answers.entrySet()) {
            Integer questionId = entry.getKey();
            Integer selectedAnswerId = entry.getValue();

            UserAnswer userAnswer = new UserAnswer();
            userAnswer.setResult(savedResult);
            userAnswer.setQuestion(questionRepository.getReferenceById(questionId)); // Dùng getReference tiết kiệm query
            userAnswer.setSelectedAnswer(answerRepository.getReferenceById(selectedAnswerId));
                   
            
            boolean isCorrect = correctAnswers.contains(selectedAnswerId);
            userAnswer.setIsCorrect(isCorrect);
            if (isCorrect) {
                correctCount++;
            }
            
            userAnswerRepository.save(userAnswer);
        }

        int totalQuestions = answers.size();
        BigDecimal score = BigDecimal.ZERO;
        if (totalQuestions > 0) {
            score = BigDecimal.valueOf(correctCount * 100.0 / totalQuestions);
        }
        
        savedResult.setScore(score);
        savedResult.setCompletedAt(Instant.now());
        
        return resultRepository.save(savedResult);
    }
}
