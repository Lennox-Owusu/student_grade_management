public class ElectiveSubject extends Subject {
    public ElectiveSubject(String code, String name) {
        super(code, name);
    }

    @Override
    public String getCategory() {
        return "Elective";
    }
}

