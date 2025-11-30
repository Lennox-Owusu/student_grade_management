//Represents a mandatory core subject that all students must take.
public class CoreSubject extends Subject {
    private final boolean mandatory = true;

    public CoreSubject(String subjectName, String subjectCode) {
        super(subjectName, subjectCode);
    }

    @Override
    public String getSubjectType() {
        return "Core";
    }

    //return true, as core subjects are mandatory.
    public boolean isMandatory() {
        return mandatory;
    }
}
