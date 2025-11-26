public abstract class Student implements Gradable {
    private static int studentCounter = 1;   // static ID generator

    private String studentId;
    private String name;

    private static final int MAX_GRADES = 50;
    private Grade[] gradeHistory = new Grade[MAX_GRADES];
    private int gradeCount = 0;

    public Student(String name) {
        this.studentId = generateStudentId();
        this.name = name;
    }

    private String generateStudentId() {
        return String.format("STU%03d", studentCounter++);
    }

    public String getStudentId() { return studentId; }
    public String getName() { return name; }

    // Implementation of Gradable
    @Override
    public void recordGrade(Grade grade) {
        if (gradeCount < gradeHistory.length) {
            gradeHistory[gradeCount++] = grade;
        } else {
            System.out.println("Grade storage full for student " + studentId);
        }
    }

    public Grade[] getGradeHistory() {
        Grade[] copy = new Grade[gradeCount];
        for (int i = 0; i < gradeCount; i++) copy[i] = gradeHistory[i];
        return copy;
    }

    public int getGradeCount() { return gradeCount; }

    public double calculateAverage() {
        if (gradeCount == 0) return 0.0;
        double sum = 0;
        for (int i = 0; i < gradeCount; i++) sum += gradeHistory[i].getScore();
        return Math.round((sum / gradeCount) * 10.0) / 10.0;
    }

    public boolean isPassing() {
        return calculateAverage() >= getPassingGrade();
    }

    public abstract double getPassingGrade();
    public abstract String getStudentType();

    @Override
    public String toString() {
        return String.format("%s | %s | %s | Avg: %.1f%% | %s | Enrolled: %d | Passing Grade: %.0f%%",
                studentId, name, getStudentType(), calculateAverage(),
                (isPassing() ? "Passing" : "Failing"), gradeCount, getPassingGrade());
    }
}
