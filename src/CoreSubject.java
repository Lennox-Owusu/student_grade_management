public class CoreSubject extends Subject {
    public CoreSubject(String code, String name) {
        super(code, name);
    }

    @Override
    public String getCategory() {
        return "Core";
    }
}

