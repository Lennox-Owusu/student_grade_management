//Represents an elective (non-mandatory) subject a student may choose.
public class ElectiveSubject extends Subject {
    private final boolean mandatory = false;


    //Constructs an elective subject with a name and code.
    public ElectiveSubject(String subjectName, String subjectCode) {
        super(subjectName, subjectCode);
    }


    //@return "Elective" to identify the subject type.
    @Override
    public String getSubjectType() {
        return "Elective";
    }

    public boolean isMandatory() {
        return mandatory;
    }
}
