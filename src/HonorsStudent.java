
public class HonorsStudent extends Student {
    private final double passingGrade = 60.0;
    private boolean honorsEligible;

    public HonorsStudent(String name, int age, String email, String phone) {
        super(name, age, email, phone);
        this.honorsEligible = false;
    }

    @Override
    public void displayStudentDetails() {
        System.out.printf("STU ID: %s%nName: %s%nType: Honors%n", getStudentId(), getName());
        System.out.printf("Avg Grade: %.2f | Status: %s%n",
                calculateAverageGrade(), (isPassing() ? "Passing" : "Failing"));
        honorsEligible = checkHonorsEligibility();
        System.out.printf("Enrolled Subjects: %d | Passing Grade: %.0f%% | Honors Eligible: %s%n%n",
                getEnrolledSubjectCount(), getPassingGrade(), (honorsEligible ? "Yes" : "No"));
    }

    @Override
    public String getStudentType() { return "Honors"; }

    @Override
    public double getPassingGrade() { return passingGrade; }

    public boolean checkHonorsEligibility() {
        return calculateAverageGrade() >= 85.0;
    }
}
