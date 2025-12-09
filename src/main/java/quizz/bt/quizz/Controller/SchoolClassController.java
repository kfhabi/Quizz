package quizz.bt.quizz.Controller;
import quizz.bt.quizz.DTO.*;
import quizz.bt.quizz.Entity.Enrollment.ClassEnrollment;
import quizz.bt.quizz.Entity.*;
import quizz.bt.quizz.Service.SchoolClassService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/v1/classes")
public class SchoolClassController {
    
    private final SchoolClassService schoolClassService;

    public SchoolClassController(SchoolClassService schoolClassService) {
        this.schoolClassService = schoolClassService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public ResponseEntity<?> createClass(@RequestBody CreateClassRequest request) {
        try{
            SchoolClass newClass = schoolClassService.createClass(request.getClassName(), request.getTeacherId());
            return new ResponseEntity<>(newClass, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinClass(@RequestBody JoinClassRequest request) {
        try {
            ClassEnrollment enrollment = schoolClassService.joinClass(request.getAccessCode(), request.getStudentId());
            return ResponseEntity.ok(enrollment);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } 
    }

    @PostMapping("/assign-quiz")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public ResponseEntity<?> assignQuizToClass(@RequestBody AssignQuizRequest request) {
        try {
            QuizAssignment assignment = schoolClassService.assignQuizToClass(
                request.getQuizId(),
                request.getClassId(),
                request.getTeacherId(),
                request.getDueDate()
            );
            return ResponseEntity.ok(assignment);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/enrolled/{studentId}")
    public ResponseEntity<List<SchoolClass>> getEnrolledClasses(@PathVariable Integer studentId)  {
        List<SchoolClass> classes = schoolClassService.getEnrolledClasses(studentId);
        return ResponseEntity.ok(classes);
    }
}
