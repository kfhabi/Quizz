package quizz.bt.quizz.Service;
import quizz.bt.quizz.Entity.*;
import quizz.bt.quizz.Entity.Enrollment.*;
import quizz.bt.quizz.Repository.Enrollment.*;
import quizz.bt.quizz.Repository.*;
import quizz.bt.quizz.Entity.Enum.*;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SchoolClassService {
    
    private final SchoolClassRepository schoolClassRepository;
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final QuizAssignmentRepository quizAssignmentRepository;
    private final ClassEnrollmentRepository classEnrollmentRepository;

    public SchoolClassService(SchoolClassRepository schoolClassRepository,
                              UserRepository userRepository,
                              QuizRepository quizRepository,
                              QuizAssignmentRepository quizAssignmentRepository,
                              ClassEnrollmentRepository classEnrollmentRepository) {
        this.schoolClassRepository = schoolClassRepository;
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
        this.quizAssignmentRepository = quizAssignmentRepository;
        this.classEnrollmentRepository = classEnrollmentRepository;
    }

    public SchoolClass createClass(String className, Integer teacherId) {

        User teacher = userRepository.findById(teacherId)
            .orElseThrow(() -> new EntityNotFoundException(""));

        if (teacher.getRole() != Role.TEACHER) {
            throw new IllegalArgumentException("");
        }

        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setClassName(className);
        schoolClass.setTeacher(teacher);
        schoolClass.setAccessCode(UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        return schoolClassRepository.save(schoolClass);
    }

    public ClassEnrollment joinClass(String accessCode, Integer studentId) {
        
        SchoolClass schoolClass = schoolClassRepository.findByAccessCode(accessCode)
            .orElseThrow(() -> new EntityNotFoundException(""));
        
        User student = userRepository.findById(studentId)
            .orElseThrow(() -> new EntityNotFoundException(""));

        ClassEnrollmentId enrollmentId = new ClassEnrollmentId(student.getId(), schoolClass.getId());

        ClassEnrollment enrollment = new ClassEnrollment();
        enrollment.setId(enrollmentId);
        enrollment.setStudent(student);
        enrollment.setSchoolClass(schoolClass);
        enrollment.setJoinedAt(Instant.now());

        return classEnrollmentRepository.save(enrollment);
    }

    public QuizAssignment assignQuizToClass(Integer quizId, Integer classId, Integer teacherId, Instant dueDate) {
        
        Quiz quiz = quizRepository.findById(quizId)
            .orElseThrow(() -> new EntityNotFoundException(""));

        SchoolClass schoolClass = schoolClassRepository.findById(classId)
            .orElseThrow(() -> new EntityNotFoundException(""));

        User assigner = userRepository.findById(teacherId)
            .orElseThrow(() -> new EntityNotFoundException(""));

        if (!schoolClass.getTeacher().getId().equals(teacherId)) {
            throw new IllegalStateException("");
        }

        QuizAssignment assignment = new QuizAssignment();
        assignment.setQuiz(quiz);
        assignment.setSchoolClass(schoolClass);
        assignment.setAssigner(assigner);
        assignment.setDueDate(dueDate);

        return quizAssignmentRepository.save(assignment);
    }

    public List<SchoolClass> getEnrolledClasses(Integer studentId) {

        List<ClassEnrollment> enrollments = classEnrollmentRepository.findByStudentId(studentId);

        return enrollments.stream()
            .map(ClassEnrollment::getSchoolClass)
            .collect(Collectors.toList());
    }
}
