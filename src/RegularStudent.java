//Represents a regular student with standard passing grade logic.
public class RegularStudent extends Student {
    private final double passingGrade = 50.0;

    //Constructs a regular student with personal details.
    public RegularStudent(String name, int age, String email, String phone) {
        super(name, age, email, phone);
    }


    @Override
    public String getStudentType() { return "Regular"; }

    @Override
    public double getPassingGrade() { return passingGrade; }
}
