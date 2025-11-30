
import java.time.LocalDate;

 //Represents a grade record for a student in a specific subject.
 //Includes unique ID, student ID, subject details, grade value, and date.
public class Grade {
    private static int gradeCounter = 0;
    private final String gradeId;
    private final String studentId;
    private final Subject subject;
    private final double grade;
    private final String date;


    //Creates a new grade record.
     public Grade(String studentId, Subject subject, double grade) {
        this.gradeId = "GRD" + (++gradeCounter);
        this.studentId = studentId;
        this.subject = subject;
        this.grade = grade;
        this.date = LocalDate.now().toString();
    }

    // Getters
    public String getGradeId() { return gradeId; }
    public String getStudentId() { return studentId; }
    public Subject getSubject() { return subject; }
    public double getGrade() { return grade; }
    public String getDate() { return date; }
}
