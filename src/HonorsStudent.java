
//Represents an honors student with additional eligibility checks.
public class HonorsStudent extends Student {
    private final double passingGrade = 60.0;


    //Constructs an honors student with personal details.
    public HonorsStudent(String name, int age, String email, String phone) {
        super(name, age, email, phone);
    }


    @Override
    public String getStudentType() { return "Honors"; }


    //return the passing grade threshold for honors students (60%)
    @Override
    public double getPassingGrade() { return passingGrade; }


    //return true if average grade >= 85
    public boolean checkHonorsEligibility() {
        return calculateAverageGrade() >= 85.0;
    }
}
