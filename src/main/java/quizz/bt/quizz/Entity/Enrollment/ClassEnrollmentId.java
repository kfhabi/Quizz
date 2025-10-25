package quizz.bt.quizz.Entity.Enrollment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ClassEnrollmentId implements Serializable {
    

    @Column(name = "class_id")
    private Integer classId;

    @Column(name = "student_id")
    private Integer studentId;

    public ClassEnrollmentId() {}

    public ClassEnrollmentId(Integer classId, Integer studentId) {
        this.classId = classId;
        this.studentId = studentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassEnrollmentId that = (ClassEnrollmentId) o;
        return Objects.equals(classId, that.classId) &&
               Objects.equals(studentId, that.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classId, studentId);
    }
}
