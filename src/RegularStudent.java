//Represents a regular student with standard passing grade logic.
public class RegularStudent extends Student {

    //Constructs a regular student with personal details.
    public RegularStudent(String name, int age, String email, String phone) {
        super(name, age, email, phone);
    }


    @Override
    public String getStudentType() { return "Regular"; }

    @Override
    public double getPassingGrade() {
        return 50.0; }
}
