
public class CoreSubject extends Subject {
    private final boolean mandatory = true;

    public CoreSubject(String subjectName, String subjectCode) {
        super(subjectName, subjectCode);
    }

    @Override
    public void displaySubjectDetails() {
        System.out.println("Core Subject: " + getSubjectName() + " (" + getSubjectCode() + ")");
    }

    @Override
    public String getSubjectType() {
        return "Core";
    }

    public boolean isMandatory() {
        return mandatory;
    }
}
