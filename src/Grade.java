
import java.time.LocalDate;

public class Grade {
    private static int gradeCounter = 0;
    private final String gradeId;
    private final String studentId;
    private final Subject subject;
    private final double grade;
    private final String date;

    public Grade(String studentId, Subject subject, double grade) {
        this.gradeId = "GRD" + (++gradeCounter);
        this.studentId = studentId;
        this.subject = subject;
        this.grade = grade;
        this.date = LocalDate.now().toString();
    }

    public void displayGradeDetails() {
        System.out.println("Grade ID: " + gradeId + ", Student ID: " + studentId +
                ", Subject: " + subject.getSubjectName() + ", Type: " + subject.getSubjectType() +
                ", Grade: " + grade + ", Date: " + date);
    }

    // Getters
    public String getGradeId() { return gradeId; }
    public String getStudentId() { return studentId; }
    public Subject getSubject() { return subject; }
    public double getGrade() { return grade; }
    public String getDate() { return date; }
}
