

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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
            System.out.println("6. Calculate Student GPA"); // NEW
            System.out.println("7. Exit"); // Exit last
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
                case 6 -> calculateStudentGPA(scanner, studentManager, gradeManager); // NEW
                case 7 -> {
                    System.out.println("Thank you for using Grade Management System!");
                    System.out.println("Goodbye!");
                    exit = true;
                }
                default -> System.out.println("Invalid choice. Please select between 1-7.");
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
        System.out.println("\nStudent Details:");
        System.out.println("Name: " + student.getName());
        System.out.println("Type: " + student.getStudentType());
        System.out.printf("Current Average: %.2f%n", student.calculateAverageGrade());
        System.out.println("─".repeat(40));
        System.out.println("\nSubject type:");
        System.out.println("1. Core Subject (Mathematics, English, Science)");
        System.out.println("2. Elective Subject (Music, Art, Physical Education)");
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
        Grade grade = new Grade(studentId, subject, gradeValue);
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
            gradeManager.addGrade(grade);
            boolean ok = student.recordGrade(gradeValue);
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
            double pct = g.getGrade();
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
    private static class GPAMap {
        final double points;
        final String letter;
        GPAMap(double points, String letter) {
            this.points = points;
            this.letter = letter;
        }
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


}
