

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.BufferedReader;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentManager studentManager = new StudentManager(50);
        GradeManager gradeManager = new GradeManager(200);
        preloadSampleStudents(studentManager);
        boolean exit = false;
        while (!exit) {
            System.out.println("\n" +
                    "╔" + "═".repeat(38) + "╗");
            System.out.println("║ STUDENT GRADE MANAGEMENT - MAIN MENU ║");
            System.out.println("╚" + "═".repeat(38) + "╝");
            System.out.println("\n1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Record Grade");
            System.out.println("4. View Grade Report");
            System.out.println("5. Export Grade Report");
            System.out.println("6. Calculate Student GPA");
            System.out.println("7. Bulk Import Grades");
            System.out.println("8. View Class Statistics");
            System.out.println("9. Search Students");
            System.out.println("10. Exit");
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
                case 5 -> exportGradeReport(scanner, studentManager, gradeManager);
                case 6 -> calculateStudentGPA(scanner, studentManager, gradeManager);
                case 7 -> bulkImportGrades(scanner, studentManager, gradeManager);
                case 8 -> viewClassStatistics(scanner, studentManager, gradeManager);
                case 9 -> searchStudents(scanner, studentManager, gradeManager);
                case 10 -> { System.out.println("Thank you for using Grade Management System!");
                    System.out.println("Goodbye!"); exit = true; }
                default -> System.out.println("Invalid choice. Please select between 1-9.");
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
        System.out.println("─".repeat(40));
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
        System.out.println("─".repeat(70));
        System.out.printf("%-8s \n %-18s \n %-8s \n %-10s \n %-8s%n",
                "STU ID", "NAME", "TYPE", "AVG GRADE", "STATUS");
        System.out.println("─".repeat(70));
        for (Student s : studentManager.getStudents()) {
            System.out.printf("%-8s │ %-18s │ %-8s │ %-10.2f │ %-8s%n",
                    s.getStudentId(), truncateName(s.getName()), s.getStudentType(),
                    s.calculateAverageGrade(), (s.isPassing() ? "Passing" : "Failing"));
            if ("Honors".equals(s.getStudentType())) {
                boolean eligible = (s instanceof HonorsStudent) && ((HonorsStudent) s).checkHonorsEligibility();
                System.out.printf(" Enrolled Subjects: %d \n Passing Grade: %.0f%% \n Honors Eligible: %s%n",
                        s.getEnrolledSubjectCount(), s.getPassingGrade(), (eligible ? "Yes" : "No"));
            } else {
                System.out.printf(" Enrolled Subjects: %d \n Passing Grade: %.0f%%%n",
                        s.getEnrolledSubjectCount(), s.getPassingGrade());
            }
            System.out.println("─".repeat(70));
        }
        System.out.println("Total Students: " + studentManager.getStudentCount());
        System.out.printf("Average Class Grade: %.2f%n", studentManager.getAverageClassGrade());
        System.out.print("\nPress Enter to continue...");
        new Scanner(System.in).nextLine();
    }

    private static String truncateName(String name) {
        return name.length() > 18 ? name.substring(0, 18 - 3) + "..." : name;
    }


    private static void recordGrade(Scanner scanner, StudentManager studentManager, GradeManager gradeManager) {
        System.out.println("\nRECORD GRADE");
        System.out.println("─".repeat(40));

        // ----- 1) Prompt for student ID with exception flow -----
        Student student = null;
        Subject subject;
        while (true) {
            System.out.print("\nEnter Student ID: ");
            String inputId = scanner.nextLine().trim();
            String studentId = inputId.toUpperCase();

            try {
                student = studentManager.findStudent(studentId);
                if (student == null) throw new StudentNotFoundException(studentId);
                // Found -> proceed
                // Show student details (like your previous version)
                System.out.println("\nStudent Details:");
                System.out.println("Name: " + student.getName());
                System.out.println("Type: " + student.getStudentType());
                System.out.printf("Current Average: %.2f%n", student.calculateAverageGrade());
                break; // exit the "find student" loop
            } catch (StudentNotFoundException snfe) {
                System.out.println("\n✗ ERROR: StudentNotFoundException");
                System.out.println("  " + snfe.getMessage());
                System.out.println();
                // Show available IDs on one line, comma separated
                Student[] all = studentManager.getStudents();
                StringBuilder ids = new StringBuilder("  Available student IDs: ");
                for (int i = 0; i < all.length; i++) {
                    ids.append(all[i].getStudentId());
                    if (i < all.length - 1) ids.append(", ");
                }
                System.out.println(ids);
                System.out.print("\nTry again? (Y/N): ");
                String retry = scanner.nextLine().trim().toUpperCase();
                if (!"Y".equals(retry)) return; // back to main menu
            }
        }

        // ----- 2) Subject type & picking (exactly like your current UI) -----
        System.out.println("\nSubject type:");
        System.out.println("1. Core Subject (Mathematics, English, Science)");
        System.out.println("2. Elective Subject (Music, Art ,Physical Education)");
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

        String[] coreSubjects = {"Mathematics", "English", "Science"};
        String[] electiveSubjects = {"Music", "Art", "Physical Education"};

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

        // ----- 3) Enter grade with exception flow and retry -----
        double gradeValue;
        while (true) {
            System.out.print("\nEnter grade (0-100): ");
            String gradeStr = scanner.nextLine().trim();
            try {
                double g = Double.parseDouble(gradeStr);
                if (g < 0 || g > 100) throw new InvalidGradeException(g);
                gradeValue = g;
                break; // valid -> proceed to confirmation
            } catch (NumberFormatException nfe) {
                System.out.println("\n✗ ERROR: NumberFormatException");
                System.out.println("  Grade must be a number between 0 and 100.");
                System.out.println("  You entered: " + gradeStr);
                System.out.print("\nTry again? (Y/N): ");
                String retry = scanner.nextLine().trim().toUpperCase();
                if (!"Y".equals(retry)) return;
            } catch (InvalidGradeException ige) {
                System.out.println("\n✗ ERROR: InvalidGradeException");
                System.out.println("  " + ige.getMessage());
                System.out.println("  You entered: " + (gradeStr.isEmpty() ? ige.getValue() : gradeStr));
                System.out.print("\nTry again? (Y/N): ");
                String retry = scanner.nextLine().trim().toUpperCase();
                if (!"Y".equals(retry)) return;
            }
        }

        // ----- 4) Confirmation and save (kept same flow you had) -----
        Grade grade = new Grade(student.getStudentId(), subject, gradeValue);

        System.out.println("\nGRADE CONFIRMATION");
        System.out.println("─".repeat(70));
        System.out.println("Grade ID: " + grade.getGradeId());
        System.out.println("Student: " + student.getStudentId() + " - " + student.getName());
        System.out.println("Subject: " + subject.getSubjectName() + " (" + subject.getSubjectType() + ")");
        System.out.printf("Grade: %.2f%n", gradeValue);
        System.out.println("Date: " + grade.getDate());
        System.out.println("─".repeat(70));
        System.out.print("Confirm grade? (Y/N): ");
        String confirm = scanner.nextLine().trim().toUpperCase();

        if (confirm.equals("Y")) {
            // (Preserved your current order)
            gradeManager.addGrade(grade);
            boolean ok = student.recordGrade(gradeValue);
            if (!ok) {
                System.out.println("Grade could not be recorded (invalid or full).");
            } else {
                student.enrollSubject(subject);
                System.out.println("\n✓ Grade recorded successfully!");
            }
        } else {
            System.out.println("\nGrade entry canceled.");
        }

        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();

}

    private static void viewGradeReport(Scanner scanner, StudentManager studentManager, GradeManager gradeManager) {
        System.out.println("\nVIEW GRADE REPORT");
        System.out.println("─".repeat(50));
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
        System.out.println("\nStudent: " + student.getStudentId() + " - " + student.getName());
        System.out.println("Type: " + student.getStudentType());
        System.out.printf("Current Average: %.2f%n", student.calculateAverageGrade());
        System.out.println("Status: " + (student.isPassing() ? "Passing" : "Failing"));
        System.out.println("─".repeat(50));
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
            System.out.println("No grades recorded for this student");
            System.out.println("─".repeat(50));
            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }
        System.out.println("\nGRADE HISTORY");
        System.out.printf("%-8s │ %-10s │ %-20s │ %-8s │ %-7s%n",
                "GRD ID", "DATE", "SUBJECT", "TYPE", "GRADE");
        System.out.println("─".repeat(70));
        for (int i = gradeManager.getGradeCount() - 1; i >= 0; i--) {
            Grade g = gradeManager.getGradeAt(i);
            if (g != null && g.getStudentId().equalsIgnoreCase(studentId)) {
                System.out.printf("%-8s │ %-10s │ %-20s │ %-8s │ %-7s%n",
                        g.getGradeId(), g.getDate(), g.getSubject().getSubjectName(),
                        g.getSubject().getSubjectType(), g.getGrade());
            }
        }
        System.out.println("─".repeat(70));
        System.out.println("Total Grades: " + totalGrades);
        System.out.printf("Core Subjects Average: %.2f%n", gradeManager.calculateCoreAverage(studentId));
        System.out.printf("Elective Subjects Average: %.2f%n", gradeManager.calculateElectiveAverage(studentId));
        System.out.printf("Overall Average: %.2f%n", gradeManager.calculateOverallAverage(studentId));
        System.out.println("\nPerformance Summary:");
        System.out.println("Passing all core subjects");
        System.out.printf("Meeting passing grade requirement (%.0f%%)%n", student.getPassingGrade());
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }


    // EXPORT GRADE REPORT (Summary / Detailed / Both) -> writes to ./reports/<filename>.txt
    private static void exportGradeReport(Scanner scanner, StudentManager studentManager, GradeManager gradeManager) {
        System.out.println("\nEXPORT GRADE REPORT");
        System.out.println("─".repeat(50));

        // 1) Ask for Student ID
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

        // 2) Gather grades for the student
        java.util.ArrayList<Grade> list = new java.util.ArrayList<>();
        for (int i = 0; i < gradeManager.getGradeCount(); i++) {
            Grade g = gradeManager.getGradeAt(i);
            if (g != null && g.getStudentId().equalsIgnoreCase(studentId)) {
                list.add(g);
            }
        }

        // Display student summary like the screenshot
        System.out.println();
        System.out.println("Student: " + student.getStudentId() + " - " + student.getName());
        System.out.println("Type: " + student.getStudentType());
        System.out.println("Total Grades: " + list.size());
        System.out.println();

        // If no grades, still allow summary export but warn
        if (list.isEmpty()) {
            System.out.println("NOTE: No grades recorded for this student yet.");
            System.out.println();
        }

        // 3) Export options
        System.out.println("Export options:");
        System.out.println("1. Summary Report (overview only)");
        System.out.println("2. Detailed Report (all grades)");
        System.out.println("3. Both");
        System.out.print("\nSelect option (1-3): ");
        int exportChoice;
        try {
            exportChoice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice.");
            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }
        if (exportChoice < 1 || exportChoice > 3) {
            System.out.println("Invalid choice.");
            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }

        // 4) Ask for filename (without extension)
        System.out.print("\nEnter filename (without extension): ");
        String baseName = scanner.nextLine().trim();
        if (baseName.isEmpty()) baseName = student.getName().toLowerCase().replaceAll("\\s+", "_") + "_report";

        // 5) Ensure ./reports/ directory
        Path reportsDir = Paths.get("./reports");
        try {
            if (!Files.exists(reportsDir)) Files.createDirectories(reportsDir);
        } catch (IOException e) {
            System.out.println("Failed to create reports directory: " + e.getMessage());
            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }

        // 6) Build target path
        String filename = baseName + ".txt";
        Path target = reportsDir.resolve(filename);

        // 7) Compute aggregates (used by summary and detailed)
        double coreAvg = gradeManager.calculateCoreAverage(studentId);
        double elecAvg = gradeManager.calculateElectiveAverage(studentId);
        double overallAvg = gradeManager.calculateOverallAverage(studentId);
        boolean passing = student.isPassing();
        boolean honorsEligible = (student instanceof HonorsStudent) && ((HonorsStudent) student).checkHonorsEligibility();

        // 8) Write file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(target.toFile()))) {

            // Header block
            writer.write("╔" + "═".repeat(50) + "╗");
            writer.newLine();
            writer.write("║ EXPORT GRADE REPORT");
            writer.newLine();
            writer.write("╚" + "═".repeat(50) + "╝");
            writer.newLine();
            writer.newLine();

            // Student overview
            writer.write("Student: " + student.getStudentId() + " - " + student.getName());
            writer.newLine();
            writer.write("Type: " + student.getStudentType());
            writer.newLine();
            writer.write(String.format("Overall Average: %.2f%%", overallAvg));
            writer.newLine();
            writer.write("Enrolled Subjects: " + student.getEnrolledSubjectCount());
            writer.newLine();
            writer.write(String.format("Passing Grade Threshold: %.0f%%", student.getPassingGrade()));
            writer.newLine();
            if ("Honors".equals(student.getStudentType())) {
                writer.write("Honors Eligible: " + (honorsEligible ? "Yes" : "No"));
                writer.newLine();
            }
            writer.write("─".repeat(70));
            writer.newLine();

            // Summary section (option 1 or 3)
            if (exportChoice == 1 || exportChoice == 3) {
                writer.write("SUMMARY REPORT");
                writer.newLine();
                writer.write("Total Grades: " + list.size());
                writer.newLine();
                writer.write(String.format("Core Subjects Average: %.2f%%", coreAvg));
                writer.newLine();
                writer.write(String.format("Elective Subjects Average: %.2f%%", elecAvg));
                writer.newLine();
                writer.write(String.format("Overall Average: %.2f%%", overallAvg));
                writer.newLine();
                writer.write("Performance Summary:");
                writer.newLine();
                writer.write(" - Status: " + (passing ? "Passing" : "Failing"));
                writer.newLine();
                writer.write(" - Meets threshold (" + (int) student.getPassingGrade() + "%): " + (passing ? "Yes" : "No"));
                writer.newLine();
                writer.write("─".repeat(70));
                writer.newLine();
            }

            // Detailed table (option 2 or 3)
            if (exportChoice == 2 || exportChoice == 3) {
                writer.write("DETAILED REPORT (All Grades)");
                writer.newLine();
                writer.write(String.format("%-8s | %-10s | %-20s | %-8s | %-7s",
                        "GRD ID", "DATE", "SUBJECT", "TYPE", "GRADE"));
                writer.newLine();
                writer.write("─".repeat(70));
                writer.newLine();

                // latest first (like your view)
                for (int i = list.size() - 1; i >= 0; i--) {
                    Grade g = list.get(i);
                    writer.write(String.format("%-8s | %-10s | %-20s | %-8s | %-7.2f",
                            g.getGradeId(), g.getDate(), g.getSubject().getSubjectName(),
                            g.getSubject().getSubjectType(), g.getGrade()));
                    writer.newLine();
                }
                writer.write("─".repeat(70));
                writer.newLine();
                writer.write("Total Grades: " + list.size());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to export report: " + e.getMessage());
            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }

        // 9) Post-write info: size, location, contents text

// 9) Post-write info: size, location, contents text (show number of grades recorded)
        long bytes = 0L;
        try {
            bytes = Files.size(target);
        } catch (IOException ignored) {
        }

        String sizeText = String.format("%.1f KB", bytes / 1024.0);
        int gradeCount = list.size(); // number of grades recorded for this student

        System.out.println("\n✓ Report exported successfully!");
        System.out.println("  File: " + target.getFileName());
        System.out.println("  Location: ./reports/");
        System.out.println("  Size: " + sizeText);

// Build 'Contains' line based on export option
        if (exportChoice == 1) {
            // Summary only (no grade rows)
            System.out.println("  Contains: overview only, averages, performance summary");
        } else {
            // Detailed or Both -> show exact number of grade records
            System.out.println("  Contains: " + gradeCount + " grades, averages, performance summary");
        }

        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

        // NEW: Calculate Student GPA (4.0 scale)
    private static void calculateStudentGPA(Scanner scanner, StudentManager studentManager, GradeManager gradeManager) {
        System.out.println("\nCALCULATE STUDENT GPA");
        System.out.println("─".repeat(50));
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
        // Collect grades for this student
        List<Grade> grades = new ArrayList<>();
        for (int i = 0; i < gradeManager.getGradeCount(); i++) {
            Grade g = gradeManager.getGradeAt(i);
            if (g != null && g.getStudentId().equalsIgnoreCase(studentId)) {
                grades.add(g);
            }
        }
        if (grades.isEmpty()) {
            System.out.println("No grades recorded for this student.");
            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }
        System.out.println("\nStudent: " + student.getStudentId() + " - " + student.getName());
        System.out.println("Type: " + student.getStudentType());
        System.out.printf("Overall Average: %.2f%%%n", student.calculateAverageGrade());
        System.out.println();
        System.out.println("GPA CALCULATION (4.0 Scale)");
        System.out.printf("%-12s │ %-6s │ %-10s%n", "Subject", "Grade", "GPA Points");
        System.out.println("─".repeat(40));
        double gpaSum = 0.0;
        for (Grade g : grades) {
            GPAMap map = toGPA(g.getGrade());
            System.out.printf("%-12s │ %5.0f%% │ %.1f (%s)%n",
                    g.getSubject().getSubjectName(), g.getGrade(), map.points, map.letter);

        }
        double cumulativeGpa = gpaSum / grades.size();
        GPAMap overallLetter = toGPA(student.calculateAverageGrade());
        // Class rank among current students by cumulative GPA
        int totalStudents = studentManager.getStudentCount();
        double[] gpas = new double[totalStudents];
        Student[] all = studentManager.getStudents();
        for (int i = 0; i < totalStudents; i++) {
            // compute GPA for each student using their recorded grades in GradeManager
            List<Grade> gs = new ArrayList<>();
            String sid = all[i].getStudentId();
            for (int j = 0; j < gradeManager.getGradeCount(); j++) {
                Grade gg = gradeManager.getGradeAt(j);
                if (gg != null && gg.getStudentId().equalsIgnoreCase(sid)) {
                    gs.add(gg);
                }
            }
            if (gs.isEmpty()) {
                gpas[i] = 0.0;
            } else {
                double sum = 0.0;
                for (Grade gg : gs) sum += toGPA(gg.getGrade()).points;
                gpas[i] = sum / gs.size();
            }
        }
        int rank = 1;
        for (double other : gpas) {
            if (other > cumulativeGpa) rank++; // higher GPA ranks above
        }
        System.out.println();
        System.out.printf("Cumulative GPA: %.2f / 4.0%n", cumulativeGpa);
        System.out.printf("Letter Grade: %s%n", overallLetter.letter);
        System.out.printf("Class Rank: %d of %d%n", rank, totalStudents);
        System.out.println();
        System.out.println("Performance Analysis:");
        System.out.println((cumulativeGpa >= 3.5 ? "✓" : "•") + " Excellent performance (3.5+ GPA)");
        boolean honorsEligible = (student instanceof HonorsStudent) && ((HonorsStudent) student).checkHonorsEligibility();
        System.out.println((honorsEligible ? "✓" : "•") + " Honors eligibility maintained");
        double classAvgGpa = 0.0;
        for (double v : gpas) classAvgGpa += v;
        classAvgGpa = totalStudents == 0 ? 0.0 : classAvgGpa / totalStudents;
        System.out.printf((cumulativeGpa >= classAvgGpa ? "✓" : "•") + " Above class average (%.2f GPA)%n", classAvgGpa);
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    // Helper structure for GPA mapping

    // Helper structure for GPA mapping
        private record GPAMap(double points, String letter) {
    }

    /**
     * Convert percentage to 4.0 GPA points + letter grade
     * Grading Scale:
     * 93–100%: 4.0 (A)
     * 90–92% : 3.7 (A-)
     * 87–89% : 3.3 (B+)
     * 83–86% : 3.0 (B)
     * 80–82% : 2.7 (B-)
     * 77–79% : 2.3 (C+)
     * 73–76% : 2.0 (C)
     * 70–72% : 1.7 (C-)
     * 67–69% : 1.3 (D+)
     * 60–66% : 1.0 (D)
     * < 60%  : 0.0 (F)
     */
    private static GPAMap toGPA(double pct) {
        if (pct >= 93) return new GPAMap(4.0, "A");
        if (pct >= 90) return new GPAMap(3.7, "A-");
        if (pct >= 87) return new GPAMap(3.3, "B+");
        if (pct >= 83) return new GPAMap(3.0, "B");
        if (pct >= 80) return new GPAMap(2.7, "B-");
        if (pct >= 77) return new GPAMap(2.3, "C+");
        if (pct >= 73) return new GPAMap(2.0, "C");
        if (pct >= 70) return new GPAMap(1.7, "C-");
        if (pct >= 67) return new GPAMap(1.3, "D+");
        if (pct >= 60) return new GPAMap(1.0, "D");
        return new GPAMap(0.0, "F");
    }


    // BULK IMPORT GRADES - Load multiple grades from CSV file (./imports/<name>.csv)
    private static void bulkImportGrades(Scanner scanner,
                                         StudentManager studentManager,
                                         GradeManager gradeManager) {
        System.out.println("\nBULK IMPORT GRADES");
        System.out.println("─".repeat(50));

        // Where to place CSV
        System.out.println("Place your CSV file in: ./imports/");
        System.out.println();
        System.out.println("CSV Format Required:");
        System.out.println("StudentID,SubjectName,SubjectType,Grade");
        System.out.println("Example: STU001,Mathematics,Core,85");
        System.out.println();

        // Ask for filename (without extension)
        System.out.print("Enter filename (without extension): ");
        String baseName = scanner.nextLine().trim();
        if (baseName.isEmpty()) {
            System.out.println("Filename cannot be empty.");
            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }

        // Ensure ./imports exists
        Path importsDir = Paths.get("./imports");
        try {
            if (!Files.exists(importsDir)) Files.createDirectories(importsDir);
        } catch (IOException e) {
            System.out.println("Failed to ensure imports directory: " + e.getMessage());
            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }

        // Resolve CSV path
        Path csvPath = importsDir.resolve(baseName + ".csv");

        // Validate file
        System.out.print("\nValidating file... ");
        if (!Files.exists(csvPath) || !Files.isRegularFile(csvPath)) {
            System.out.println("✗");
            System.out.println("File not found: " + csvPath);
            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        } else {
            System.out.println("✓");
        }

        // Prepare log file
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path logPath = importsDir.resolve("import_log_" + ts + ".txt");

        System.out.println("Processing grades...");

        int totalRows = 0;
        int success = 0;
        int failed = 0;
        java.util.List<String> failures = new java.util.ArrayList<>();

        // Read CSV
        try (BufferedReader br = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8);
             BufferedWriter log = Files.newBufferedWriter(logPath, StandardCharsets.UTF_8)) {

            String line;
            int rowNum = 0;

            while ((line = br.readLine()) != null) {
                rowNum++;
                line = line.trim();
                if (line.isEmpty()) continue;

                // Skip header automatically if present
                if (rowNum == 1 && line.toLowerCase().startsWith("studentid,")) {
                    continue;
                }

                totalRows++;

                String[] parts = line.split(",", -1); // keep empty tokens
                if (parts.length != 4) {
                    failed++;
                    String reason = "Row " + rowNum + ": Invalid column count (" + parts.length + ")";
                    failures.add(reason);
                    log.write(reason); log.newLine();
                    continue;
                }

                String sid     = parts[0].trim().toUpperCase();
                String subj    = parts[1].trim();
                String type    = parts[2].trim();
                String gradeStr= parts[3].trim();

                // Validate student
                Student student = studentManager.findStudent(sid);
                if (student == null) {
                    failed++;
                    String reason = "Row " + rowNum + ": Invalid student ID (" + sid + ")";
                    failures.add(reason);
                    log.write(reason); log.newLine();
                    continue;
                }

                // Validate subject type
                boolean isCore = "Core".equalsIgnoreCase(type);
                boolean isElective = "Elective".equalsIgnoreCase(type);
                if (!isCore && !isElective) {
                    failed++;
                    String reason = "Row " + rowNum + ": Invalid subject type (" + type + ")";
                    failures.add(reason);
                    log.write(reason); log.newLine();
                    continue;
                }

                // Parse and validate grade
                double pct;
                try {
                    pct = Double.parseDouble(gradeStr);
                } catch (NumberFormatException nfe) {
                    failed++;
                    String reason = "Row " + rowNum + ": Grade not a number (" + gradeStr + ")";
                    failures.add(reason);
                    log.write(reason); log.newLine();
                    continue;
                }
                if (pct < 0 || pct > 100) {
                    failed++;
                    String reason = "Row " + rowNum + ": Grade out of range (" + gradeStr + ")";
                    failures.add(reason);
                    log.write(reason); log.newLine();
                    continue;
                }

                // Build Subject instance
                Subject subject = isCore
                        ? new CoreSubject(subj, "C" + (int) (Math.random() * 1000))
                        : new ElectiveSubject(subj, "E" + (int) (Math.random() * 1000));

                // Create Grade and persist
                Grade grade = new Grade(sid, subject, pct);
                gradeManager.addGrade(grade);

                // Record grade via Gradable
                boolean ok = student.recordGrade(pct);
                if (!ok) {
                    failed++;
                    String reason = "Row " + rowNum + ": Student grade storage full or invalid";
                    failures.add(reason);
                    log.write(reason); log.newLine();
                    continue;
                }

                // Enroll subject (Set prevents duplicates)
                student.enrollSubject(subject);

                success++;
            }

            // Write end of log summary
            log.newLine();
            log.write("IMPORT SUMMARY"); log.newLine();
            log.write("Total Rows: " + totalRows); log.newLine();
            log.write("Successfully Imported: " + success); log.newLine();
            log.write("Failed: " + failed); log.newLine();

        } catch (IOException e) {
            System.out.println("Import failed due to I/O error: " + e.getMessage());
            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }

        // Render summary like screenshot
        System.out.println();
        System.out.println("IMPORT SUMMARY");
        System.out.println("─".repeat(50));
        System.out.println("Total Rows: " + totalRows);
        System.out.println("Successfully Imported: " + success);
        System.out.println("Failed: " + failed);

        if (!failures.isEmpty()) {
            System.out.println();
            System.out.println("Failed Records:");
            for (String f : failures) System.out.println(f);
        }

        System.out.println();
        System.out.println("✓ Import completed!");
        System.out.println(success + " grades added to system");
        System.out.println("See " + logPath.getFileName() + " for details");

        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    // VIEW CLASS STATISTICS - class-wide analytics (distribution, mean/median/mode/std dev, subject & type comparisons)
    private static void viewClassStatistics(Scanner scanner,
                                            StudentManager studentManager,
                                            GradeManager gradeManager) {

        System.out.println("\nCLASS STATISTICS");
        System.out.println("─".repeat(60));

        // Collect all grades
        java.util.List<Grade> allGrades = new java.util.ArrayList<>();
        for (int i = 0; i < gradeManager.getGradeCount(); i++) {
            Grade g = gradeManager.getGradeAt(i);
            if (g != null) allGrades.add(g);
        }

        int totalStudents = studentManager.getStudentCount();
        int totalGrades = allGrades.size();

        System.out.println("\nTotal Students: " + totalStudents);
        System.out.println("Total Grades Recorded: " + totalGrades);

        if (totalGrades == 0) {
            System.out.println("\nNo grades in the system yet.");
            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
            return;
        }

        // -------- Grade distribution (A/B/C/D/F bands) --------
        int a = 0, b = 0, c = 0, d = 0, f = 0;
        double min = 101, max = -1;
        Grade minG = null, maxG = null;

        for (Grade g : allGrades) {
            double v = g.getGrade();
            if (v > max) { max = v; maxG = g; }
            if (v < min) { min = v; minG = g; }
            if (v >= 90) a++;
            else if (v >= 80) b++;
            else if (v >= 70) c++;
            else if (v >= 60) d++;
            else f++;
        }

        System.out.println("\nGRADE DISTRIBUTION");
        System.out.println("─".repeat(60));
        printBand("90–100% (A):", a, totalGrades);
        printBand("80–89%  (B):", b, totalGrades);
        printBand("70–79%  (C):", c, totalGrades);
        printBand("60–69%  (D):", d, totalGrades);
        printBand("0–59%   (F):", f, totalGrades);

        // -------- Statistical analysis --------
        // Mean
        double sum = 0.0;
        for (Grade g : allGrades) sum += g.getGrade();
        double mean = sum / totalGrades;

        // Median
        double[] arr = new double[totalGrades];
        for (int i = 0; i < totalGrades; i++) arr[i] = allGrades.get(i).getGrade();
        java.util.Arrays.sort(arr);
        double median = (totalGrades % 2 == 1)
                ? arr[totalGrades / 2]
                : (arr[totalGrades / 2 - 1] + arr[totalGrades / 2]) / 2.0;

        // Mode (most frequent whole-number grade; ties -> highest value)
        java.util.Map<Integer, Integer> freq = new java.util.HashMap<>();
        for (double v : arr) {
            int r = (int) Math.round(v);
            freq.put(r, freq.getOrDefault(r, 0) + 1);
        }
        int modeVal = -1, bestFreq = -1;
        for (java.util.Map.Entry<Integer, Integer> e : freq.entrySet()) {
            int val = e.getKey(), cnt = e.getValue();
            if (cnt > bestFreq || (cnt == bestFreq && val > modeVal)) {
                bestFreq = cnt; modeVal = val;
            }
        }

        // Population Standard Deviation
        double sq = 0.0;
        for (double v : arr) sq += Math.pow(v - mean, 2);
        double std = Math.sqrt(sq / totalGrades);

        System.out.println("\nSTATISTICAL ANALYSIS");
        System.out.println("─".repeat(60));
        System.out.printf("Mean (Average):   %.1f%%%n", mean);
        System.out.printf("Median:           %.1f%%%n", median);
        System.out.printf("Mode:             %.1f%%%n", (double) modeVal);
        System.out.printf("Standard Deviation: %.1f%%%n", std);
        System.out.printf("Range:            %.1f%% (%.0f%% – %.0f%%)%n", (max - min), min, max);

        // Highest/Lowest grade records
        System.out.printf("%nHighest Grade: %.0f%% (%s - %s)%n",
                maxG.getGrade(), maxG.getStudentId(), maxG.getSubject().getSubjectName());
        System.out.printf("Lowest  Grade: %.0f%% (%s - %s)%n",
                minG.getGrade(), minG.getStudentId(), minG.getSubject().getSubjectName());

        // -------- Subject performance (Core/Elective + per subject) --------
        double coreSum = 0, coreCnt = 0, elecSum = 0, elecCnt = 0;
        java.util.Map<String, double[]> subjectAgg = new java.util.HashMap<>(); // name -> [sum, count]
        for (Grade g : allGrades) {
            String type = g.getSubject().getSubjectType();
            if ("Core".equalsIgnoreCase(type)) { coreSum += g.getGrade(); coreCnt++; }
            else if ("Elective".equalsIgnoreCase(type)) { elecSum += g.getGrade(); elecCnt++; }

            String name = g.getSubject().getSubjectName();
            double[] sc = subjectAgg.getOrDefault(name, new double[]{0.0, 0.0});
            sc[0] += g.getGrade(); sc[1] += 1.0;
            subjectAgg.put(name, sc);
        }
        double coreAvg = coreCnt == 0 ? 0.0 : coreSum / coreCnt;
        double elecAvg = elecCnt == 0 ? 0.0 : elecSum / elecCnt;

        System.out.println("\nSUBJECT PERFORMANCE");
        System.out.println("─".repeat(60));
        System.out.printf("Core Subjects:     %.1f%% average%n", coreAvg);
        printSubjectAvg(subjectAgg, "Mathematics");
        printSubjectAvg(subjectAgg, "English");
        printSubjectAvg(subjectAgg, "Science");
        System.out.println();
        System.out.printf("Elective Subjects: %.1f%% average%n", elecAvg);
        printSubjectAvg(subjectAgg, "Music");
        printSubjectAvg(subjectAgg, "Art");
        printSubjectAvg(subjectAgg, "Physical Education");

        // -------- Student type comparison --------
        // Include only students who have recorded at least one grade
        java.util.Map<String, Integer> gradesPerStudent = new java.util.HashMap<>();
        for (Grade g : allGrades) {
            gradesPerStudent.put(g.getStudentId(), gradesPerStudent.getOrDefault(g.getStudentId(), 0) + 1);
        }

        double regSumAvg = 0.0; int regCount = 0;
        double honSumAvg = 0.0; int honCount = 0;
        for (Student s : studentManager.getStudents()) {
            Integer scnt = gradesPerStudent.get(s.getStudentId());
            if (scnt == null || scnt == 0) continue; // skip students with no grades
            double avg = s.calculateAverageGrade();
            if ("Regular".equalsIgnoreCase(s.getStudentType())) { regSumAvg += avg; regCount++; }
            else if ("Honors".equalsIgnoreCase(s.getStudentType())) { honSumAvg += avg; honCount++; }
        }

        System.out.println("\nSTUDENT TYPE COMPARISON");
        System.out.println("─".repeat(60));
        System.out.printf("Regular Students: %.1f%% average (%d students)%n",
                regCount == 0 ? 0.0 : regSumAvg / regCount, regCount);
        System.out.printf("Honors Students:  %.1f%% average (%d students)%n",
                honCount == 0 ? 0.0 : honSumAvg / honCount, honCount);

        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    // ----- helpers for Class Statistics -----
    private static void printBand(String label, int count, int total) {
        double pct = total == 0 ? 0.0 : (count * 100.0 / total);
        String bar = buildBar(count, total, 28); // 28-char bar to fit nicely
        System.out.printf("%-13s %s  %4.1f%% (%d grades)%n", label, bar, pct, count);
    }

    private static String buildBar(int count, int total, int width) {
        int filled = (total == 0) ? 0 : (int) Math.round((count * 1.0 / total) * width);
        if (filled < 0) filled = 0; if (filled > width) filled = width;
        String filledPart = "█".repeat(filled);
        String emptyPart  = "░".repeat(width - filled);
        return filledPart + emptyPart;
    }

    private static void printSubjectAvg(java.util.Map<String, double[]> agg, String subject) {
        double[] sc = agg.get(subject);
        if (sc != null && sc[1] > 0) {
            double avg = sc[0] / sc[1];
            System.out.printf("%s: %6.1f%%%s%n", subject, avg, "");
        } else {
            // If subject not present, you can omit the line or show 0.0%
            // System.out.printf("%s: %6.1f%%%n", subject, 0.0);
        }
    }

    // ======================= SEARCH STUDENTS (NEW) =======================
    private static void searchStudents(Scanner scanner,
                                       StudentManager studentManager,
                                       GradeManager gradeManager) {

        while (true) { // allows "New search" to loop
            System.out.println("\nSEARCH STUDENTS");
            System.out.println("─".repeat(50));

            System.out.println("\nSearch options:");
            System.out.println("1. By Student ID");
            System.out.println("2. By Name (partial match)");
            System.out.println("3. By Grade Range");
            System.out.println("4. By Student Type");
            System.out.print("\nSelect option (1-4): ");

            int opt;
            try {
                opt = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter 1-4.");
                continue;
            }
            if (opt < 1 || opt > 4) {
                System.out.println("Invalid choice. Please enter 1-4.");
                continue;
            }

            // Gather results
            java.util.List<Student> results = new java.util.ArrayList<>();
            Student[] all = studentManager.getStudents();

            switch (opt) {
                case 1 -> {
                    System.out.print("\nEnter ID (partial or full): ");
                    String q = scanner.nextLine().trim().toUpperCase();
                    for (Student s : all) {
                        if (s.getStudentId().toUpperCase().contains(q)) results.add(s);
                    }
                }
                case 2 -> {
                    System.out.print("\nEnter name (partial or full): ");
                    String q = scanner.nextLine().trim().toLowerCase();
                    for (Student s : all) {
                        if (s.getName().toLowerCase().contains(q)) results.add(s);
                    }
                }
                case 3 -> {
                    double min, max;
                    try {
                        System.out.print("\nEnter minimum average (0-100): ");
                        min = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Enter maximum average (0-100): ");
                        max = Double.parseDouble(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number. Try again.");
                        continue;
                    }
                    if (min > max) {
                        double t = min; min = max; max = t; // swap
                    }
                    for (Student s : all) {
                        double avg = s.calculateAverageGrade();
                        if (avg >= min && avg <= max) results.add(s);
                    }
                }
                case 4 -> {
                    System.out.print("\nEnter type (Regular/Honors): ");
                    String q = scanner.nextLine().trim();
                    for (Student s : all) {
                        if (s.getStudentType().equalsIgnoreCase(q)) results.add(s);
                    }
                }
            }

            printSearchResults(results);

            // ---- Post-search actions ----
            while (true) {
                System.out.println("\nActions:");
                System.out.println("1. View full details for a student");
                System.out.println("2. Export search results");
                System.out.println("3. New search");
                System.out.println("4. Return to main menu");
                System.out.print("\nEnter choice: ");

                int action;
                try {
                    action = Integer.parseInt(scanner.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid choice. Enter 1-4.");
                    continue;
                }

                if (action == 1) {
                    // Reuse your existing detailed report flow
                    // (It will prompt for Student ID, which matches your current design.)
                    viewGradeReport(scanner, studentManager, gradeManager);
                } else if (action == 2) {
                    exportSearchResults(results);
                } else if (action == 3) {
                    // new search -> break actions loop and restart search flow
                    break;
                } else if (action == 4) {
                    // return to main menu
                    return;
                } else {
                    System.out.println("Invalid choice. Enter 1-4.");
                }
            }
        }
    }

    private static void printSearchResults(java.util.List<Student> results) {
        System.out.println("\nSEARCH RESULTS (" + results.size() + " found)");
        System.out.println("─".repeat(50));
        System.out.printf("%-8s │ %-18s │ %-8s │ %-5s%n", "STU ID", "NAME", "TYPE", "AVG");
        System.out.println("─".repeat(50));

        if (results.isEmpty()) {
            System.out.println("(no matches)");
            System.out.println("─".repeat(50));
            return;
        }

        for (Student s : results) {
            System.out.printf("%-8s │ %-18s │ %-8s │ %5.1f%%%n",
                    s.getStudentId(),
                    truncateName(s.getName()),
                    s.getStudentType(),
                    s.calculateAverageGrade());
        }
        System.out.println("─".repeat(50));
    }

    private static void exportSearchResults(java.util.List<Student> results) {
        // Ensure ./reports exists
        Path reportsDir = Paths.get("./reports");
        try {
            if (!Files.exists(reportsDir)) Files.createDirectories(reportsDir);
        } catch (IOException e) {
            System.out.println("Failed to create reports directory: " + e.getMessage());
            return;
        }

        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path target = reportsDir.resolve("search_results_" + ts + ".txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(target.toFile()))) {
            writer.write("SEARCH RESULTS (" + results.size() + " found)"); writer.newLine();
            writer.write("─".repeat(50)); writer.newLine();
            writer.write(String.format("%-8s │ %-18s │ %-8s │ %-5s", "STU ID", "NAME", "TYPE", "AVG")); writer.newLine();
            writer.write("─".repeat(50)); writer.newLine();

            for (Student s : results) {
                writer.write(String.format("%-8s │ %-18s │ %-8s │ %5.1f%%",
                        s.getStudentId(),
                        truncateName(s.getName()),
                        s.getStudentType(),
                        s.calculateAverageGrade()));
                writer.newLine();
            }
            writer.write("─".repeat(50)); writer.newLine();

        } catch (IOException e) {
            System.out.println("Failed to export search results: " + e.getMessage());
            return;
        }

        long bytes = 0;
        try { bytes = Files.size(target); } catch (IOException ignored) {}
        String sizeText = String.format("%.1f KB", bytes / 1024.0);

        System.out.println("\n✓ Search results exported!");
        System.out.println(" File: " + target.getFileName());
        System.out.println(" Location: ./reports/");
        System.out.println(" Size: " + sizeText);
    }

    //Custom Exceptions
    private static class StudentNotFoundException extends Exception {
        public StudentNotFoundException(String id) {
            super("Student with ID '" + id + "' not found in the system.");
        }
    }

    private static class InvalidGradeException extends Exception {
        private final double value;
        public InvalidGradeException(double value) {
            super("Grade must be between 0 and 100.");
            this.value = value;
        }
        public double getValue() { return value; }
    }

}
