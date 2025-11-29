
public class RegularStudent extends Student {
    private final double passingGrade = 50.0;

    public RegularStudent(String name, int age, String email, String phone) {
        super(name, age, email, phone);
    }

    @Override
    public void displayStudentDetails() {
        System.out.printf("STU ID: %s%nName: %s%nType: Regular%n", getStudentId(), getName());
        System.out.printf("Avg Grade: %.2f | Status: %s%n",
                calculateAverageGrade(), (isPassing() ? "Passing" : "Failing"));
        System.out.printf("Enrolled Subjects: %d | Passing Grade: %.0f%%%n%n",
                getEnrolledSubjectCount(), getPassingGrade());
    }

    @Override
    public String getStudentType() { return "Regular"; }

    @Override
    public double getPassingGrade() { return passingGrade; }
}
