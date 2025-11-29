
public class StudentManager {
    private Student[] students;
    private int studentCount;

    public StudentManager(int size) {
        students = new Student[size];
        studentCount = 0;
    }

    public void addStudent(Student student) {
        if (studentCount < students.length) {
            students[studentCount++] = student;
        } else {
            System.out.println("Student list is full.");
        }
    }

    public Student findStudent(String studentId) {
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getStudentId().equalsIgnoreCase(studentId)) {
                return students[i];
            }
        }
        return null;
    }

    public Student getStudent(int index) {
        if (index < 0 || index >= studentCount) return null;
        return students[index];
    }

    public Student[] getStudents() {
        Student[] copy = new Student[studentCount];
        for (int i = 0; i < studentCount; i++) copy[i] = students[i];
        return copy;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public double getAverageClassGrade() {
        if (studentCount == 0) return 0.0;
        double sum = 0;
        for (int i = 0; i < studentCount; i++) sum += students[i].calculateAverageGrade();
        return sum / studentCount;
    }

    public void viewAllStudents() {
        System.out.println("\nSTUDENT LISTING\n");
        for (int i = 0; i < studentCount; i++) {
            students[i].displayStudentDetails();
        }
        System.out.println("Total Students: " + studentCount);
        System.out.printf("Average Class Grade: %.2f%n", getAverageClassGrade());
    }
}
