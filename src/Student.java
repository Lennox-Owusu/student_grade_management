
import java.util.HashSet;
import java.util.Set;

public abstract class Student {
    private final String studentId;
    private final String name;
    private final int age;
    private final String email;
    private final String phone;
    private final String status;

    protected double[] grades;
    protected int gradeCount;

    private final Set<String> enrolledSubjectCodes = new HashSet<>();

    private static int studentCounter = 0;

    public Student(String name, int age, String email, String phone) {
        this.studentId = String.format("STU%03d", ++studentCounter);
        this.name = name;
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.status = "Active";
        this.grades = new double[50];
        this.gradeCount = 0;
    }

    public abstract void displayStudentDetails();
    public abstract String getStudentType();
    public abstract double getPassingGrade();

    public void addGrade(double grade) {
        if (gradeCount < grades.length) {
            grades[gradeCount++] = grade;
        }
    }

    public double calculateAverageGrade() {
        if (gradeCount == 0) return 0.0;
        double sum = 0;
        for (int i = 0; i < gradeCount; i++) sum += grades[i];
        return sum / gradeCount;
    }

    public boolean isPassing() {
        return calculateAverageGrade() >= getPassingGrade();
    }

    public void enrollSubject(Subject subject) {
        if (subject != null) {
            enrolledSubjectCodes.add(subject.getSubjectCode());
        }
    }

    public int getEnrolledSubjectCount() {
        return enrolledSubjectCodes.size();
    }

    public String getStudentId() { return studentId; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getStatus() { return status; }
}
