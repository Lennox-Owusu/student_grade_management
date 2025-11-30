
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentManager studentManager = new StudentManager(50);
        GradeManager gradeManager = new GradeManager(200);

        preloadSampleStudents(studentManager);

        boolean exit = false;
        while (!exit) {
            System.out.println("\n======================================");
            System.out.println("STUDENT GRADE MANAGEMENT - MAIN MENU");
            System.out.println("======================================");
            System.out.println("\n1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Record Grade");
            System.out.println("4. View Grade Report");
            System.out.println("5. Exit");
            System.out.print("\nEnter choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> addStudent(scanner, studentManager);
                case 2 -> viewStudents(studentManager);
                case 3 -> recordGrade(scanner, studentManager, gradeManager);
                case 4 -> viewGradeReport(scanner, studentManager, gradeManager);
                case 5 -> {
                    System.out.println("Thank you for using Grade Management System!");
                    System.out.println("Goodbye!");
                    exit = true;
                }
                default -> System.out.println("Invalid choice. Please select between 1-5.");
            }
        }
        scanner.close();
    }

    private static void preloadSampleStudents(StudentManager studentManager) {
        studentManager.addStudent(new RegularStudent("Alice Johnson", 16, "alice@school.edu", "555-1111"));
        studentManager.addStudent(new HonorsStudent("Bob Smith", 17, "bob@school.edu", "555-2222"));
        studentManager.addStudent(new RegularStudent("Carol Martinez", 15, "carol@school.edu", "555-3333"));
        studentManager.addStudent(new HonorsStudent("David Chen", 18, "david@school.edu", "555-4444"));
        studentManager.addStudent(new RegularStudent("Emma Wilson", 16, "emma@school.edu", "555-5555"));
    }


    private static void addStudent(Scanner scanner, StudentManager studentManager) {
        System.out.println("\nADD STUDENT");
        System.out.println("----------------------------------------");
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter student age: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter student email: ");
        String email = scanner.nextLine();
        System.out.print("Enter student phone: ");
        String phone = scanner.nextLine();

        System.out.println("\nStudent type:");
        System.out.println("1. Regular Student (Passing grade: 50%): ");
        System.out.println("2. Honors Student (Passing grade: 60%, honors recognition): ");
        System.out.print("Select type (1-2):");
        int type = Integer.parseInt(scanner.nextLine());

        Student student;
        if (type == 1) {
            student = new RegularStudent(name, age, email, phone);
        } else if (type == 2) {
            student = new HonorsStudent(name, age, email, phone);
        } else {
            System.out.println("Invalid type selected.");
            return;
        }

        studentManager.addStudent(student);
        //  Confirmation block based on type of student
        System.out.println("\nStudent added successfully!");
        System.out.println("Student ID: " + student.getStudentId());
        System.out.println("Name: " + student.getName());
        System.out.println("Type: " + student.getStudentType());
        System.out.println("Age: " + student.getAge());
        System.out.println("Email: " + student.getEmail());
        System.out.printf("Passing Grade: %.0f%%%n", student.getPassingGrade());

        if (student instanceof HonorsStudent) {
            boolean eligible = ((HonorsStudent) student).checkHonorsEligibility();
            System.out.println("Honors Eligible: " + (eligible ? "Yes" : "No"));
        }

        System.out.println("Status: Active");

        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }


    private static void viewStudents(StudentManager studentManager) {
        if (studentManager.getStudentCount() == 0) {
            System.out.println("No students to display yet.");
            return;
        }

        System.out.println("\nSTUDENT LISTING");
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("%-8s | %-18s | %-8s | %-10s | %-8s%n",
                "STU ID", "NAME", "TYPE", "AVG GRADE", "STATUS");
        System.out.println("--------------------------------------------------------------------------");

        for (Student s : studentManager.getStudents()) {
            System.out.printf("%-8s | %-18s | %-8s | %-10.2f | %-8s%n",
                    s.getStudentId(), truncateName(s.getName()), s.getStudentType(),
                    s.calculateAverageGrade(), (s.isPassing() ? "Passing" : "Failing"));

            if ("Honors".equals(s.getStudentType())) {
                boolean eligible = (s instanceof HonorsStudent) && ((HonorsStudent) s).checkHonorsEligibility();
                System.out.printf("     Enrolled Subjects: %d | Passing Grade: %.0f%% | Honors Eligible: %s%n",
                        s.getEnrolledSubjectCount(), s.getPassingGrade(), (eligible ? "Yes" : "No"));
            } else {
                System.out.printf("     Enrolled Subjects: %d | Passing Grade: %.0f%%%n",
                        s.getEnrolledSubjectCount(), s.getPassingGrade());
            }
            System.out.println("--------------------------------------------------------------------------");
        }

        System.out.println("Total Students: " + studentManager.getStudentCount());
        System.out.printf("Average Class Grade: %.2f%n", studentManager.getAverageClassGrade());


        System.out.print("\nPress Enter to continue...");
        new Scanner(System.in).nextLine();
    }

    // Helper to truncate long names
    private static String truncateName(String name) {
        return name.length() > 18 ? name.substring(0, 18 - 3) + "..." : name;


    }


    private static void recordGrade(Scanner scanner, StudentManager studentManager, GradeManager gradeManager) {
        System.out.println("\nRECORD GRADE");
        System.out.println("----------------------------------------");
        System.out.print("\nEnter Student ID: ");
        String inputId = scanner.nextLine().trim();
        String studentId = inputId.toUpperCase();

        Student student = studentManager.findStudent(studentId);
        if (student == null) {
            System.out.println("Student not found!");
            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }

        // Show student details before proceeding
        System.out.println("\nStudent Details:");
        System.out.println("Name: " + student.getName());
        System.out.println("Type: " + student.getStudentType());
        System.out.printf("Current Average: %.2f%n", student.calculateAverageGrade());
        System.out.println("----------------------------------------");

        // Subject type selection
        System.out.println("\nSubject type:");
        System.out.println("1. Core Subject (Mathematics, English, Science)");
        System.out.println("2. Elective Subject (Music, Art, Physical Education");
        System.out.print("\nSelect type (1-2): ");
        int subjectType;
        try {
            subjectType = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid subject type.");
            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }

        // Show available subjects based on type
        String[] coreSubjects = {"Mathematics", "English", "Science"};
        String[] electiveSubjects = {"Music", "Art", "Physical Education"};
        Subject subject = null;

        if (subjectType == 1) {
            System.out.println("\nAvailable Core Subjects:");
            for (int i = 0; i < coreSubjects.length; i++) {
                System.out.println((i + 1) + ". " + coreSubjects[i]);
            }
            System.out.print("\nSelect subject (1-3): ");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice >= 1 && choice <= 3) {
                subject = new CoreSubject(coreSubjects[choice - 1], "C" + (int) (Math.random() * 1000));
            } else {
                System.out.println("Invalid choice.");
                System.out.print("\nPress Enter to continue...");
                scanner.nextLine();
                return;
            }
        } else if (subjectType == 2) {
            System.out.println("\nAvailable Elective Subjects:");
            for (int i = 0; i < electiveSubjects.length; i++) {
                System.out.println((i + 1) + ". " + electiveSubjects[i]);
            }
            System.out.print("\nSelect subject (1-3): ");
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice >= 1 && choice <= 3) {
                subject = new ElectiveSubject(electiveSubjects[choice - 1], "E" + (int) (Math.random() * 1000));
            } else {
                System.out.println("Invalid choice.");
                System.out.print("\nPress Enter to continue...");
                scanner.nextLine();
                return;
            }
        } else {
            System.out.println("Invalid subject type.");
            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }

        // Enter grade
        System.out.print("Enter grade (0-100): ");
        double gradeValue;
        try {
            gradeValue = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid grade. Must be a number between 0 and 100.");
            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }

        if (gradeValue < 0 || gradeValue > 100) {
            System.out.println("Invalid grade. Must be between 0 and 100.");
            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }

        //Prepare grade for confirmation
        Grade grade = new Grade(studentId, subject, gradeValue);

        //Show confirmation block
        System.out.println("\nGRADE CONFIRMATION");
        System.out.println("----------------------------------------");
        System.out.println("Grade ID: " + grade.getGradeId());
        System.out.println("Student: " + student.getStudentId() + " - " + student.getName());
        System.out.println("Subject: " + subject.getSubjectName() + " (" + subject.getSubjectType() + ")");
        System.out.printf("Grade: %.2f%n", gradeValue);
        System.out.println("Date: " + grade.getDate());
        System.out.println("----------------------------------------");
        System.out.print("Confirm grade? (Y/N): ");
        String confirm = scanner.nextLine().trim().toUpperCase();


        if (confirm.equals("Y")) {
            gradeManager.addGrade(grade);
            boolean ok = student.recordGrade(gradeValue); // Use interface method
            if (!ok) {
                System.out.println("Grade could not be recorded (invalid or full).");
            } else {
                student.enrollSubject(subject);
                System.out.println("\nGrade recorded successfully!");
            }
        } else {
            System.out.println("\nGrade entry canceled.");
        }


        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }





    private static void viewGradeReport(Scanner scanner, StudentManager studentManager, GradeManager gradeManager) {
        System.out.println("\nVIEW GRADE REPORT");
        System.out.println("----------------------------------------");
        System.out.print("Enter Student ID: ");
        String inputId = scanner.nextLine().trim();
        String studentId = inputId.toUpperCase();

        Student student = studentManager.findStudent(studentId);
        if (student == null) {
            System.out.println("Student not found!");
            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }

        //Show student details
        System.out.println("\nStudent: " + student.getStudentId() + " - " + student.getName());
        System.out.println("Type: " + student.getStudentType());
        System.out.printf("Current Average: %.2f%n", student.calculateAverageGrade());
        System.out.println("Status: " + (student.isPassing() ? "Passing" : "Failing"));
        System.out.println("----------------------------------------");

        //Check if student has any grades
        boolean hasGrades = false;
        int totalGrades = 0;
        for (int i = 0; i < gradeManager.getGradeCount(); i++) {
            Grade g = gradeManager.getGradeAt(i);
            if (g != null && g.getStudentId().equalsIgnoreCase(studentId)) {
                hasGrades = true;
                totalGrades++;
            }
        }

        if (!hasGrades) {
            System.out.println("----------------------------------------");
            System.out.println("No grades recorded for this student");
            System.out.println("----------------------------------------");
            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }

        //Show grade history table
        System.out.println("\nGRADE HISTORY");
        System.out.printf("%-8s | %-10s | %-20s | %-8s | %-7s%n",
                "GRD ID", "DATE", "SUBJECT", "TYPE", "GRADE");
        System.out.println("--------------------------------------------------------------------------");

        for (int i = gradeManager.getGradeCount() - 1; i >= 0; i--) {
            Grade g = gradeManager.getGradeAt(i);
            if (g != null && g.getStudentId().equalsIgnoreCase(studentId)) {
                System.out.printf("%-8s | %-10s | %-20s | %-8s | %-7.2f%n",
                        g.getGradeId(), g.getDate(), g.getSubject().getSubjectName(),
                        g.getSubject().getSubjectType(), g.getGrade());
            }
        }

        System.out.println("--------------------------------------------------------------------------");
        System.out.println("Total Grades: " + totalGrades);
        System.out.printf("Core Subjects Average: %.2f%n", gradeManager.calculateCoreAverage(studentId));
        System.out.printf("Elective Subjects Average: %.2f%n", gradeManager.calculateElectiveAverage(studentId));
        System.out.printf("Overall Average: %.2f%n", gradeManager.calculateOverallAverage(studentId));

        //Performance Summary
        System.out.println("\nPerformance Summary:");
        System.out.println("Passing all core subjects");
        System.out.printf("Meeting passing grade requirement (%.0f%%)%n", student.getPassingGrade());

        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

}