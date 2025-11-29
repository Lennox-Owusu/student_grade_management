
public abstract class Subject {
    private final String subjectName;
    private final String subjectCode;

    public Subject(String subjectName, String subjectCode) {
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
    }

    public abstract void displaySubjectDetails();
    public abstract String getSubjectType();

    public String getSubjectName() { return subjectName; }
    public String getSubjectCode() { return subjectCode; }
}
