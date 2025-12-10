
package org.testing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class StudentManagerTest {

    // ---- Test-only minimal student ----
    static class TestStudent extends Student {
        public TestStudent(String name, int age, String email) {
            super(name, age, email);
        }

        @Override
        public String getStudentType() {
            return "Regular"; // label is irrelevant for StudentManager tests
        }

        @Override
        public double getPassingGrade() {
            return 50.0;
        }

        public boolean recordGrade(double grade) {
            if (Double.isNaN(grade) || grade < 0.0 || grade > 100.0) return false;
            if (gradeCount >= grades.length) return false;
            grades[gradeCount++] = grade;
            return true;
        }

        @Override
        public double calculateAverageGrade() {
            if (gradeCount == 0) return 0.0;
            double sum = 0.0;
            for (int i = 0; i < gradeCount; i++) sum += grades[i];
            return sum / gradeCount;
        }
    }

    @Test
    void addStudent_withinCapacity_incrementsCount() {
        StudentManager mgr = new StudentManager(2);
        TestStudent s1 = new TestStudent("Ama Owusu", 18, "ama@example.com");
        TestStudent s2 = new TestStudent("Kofi Mensah", 19, "kofi@example.com");

        mgr.addStudent(s1);
        mgr.addStudent(s2);

        assertEquals(2, mgr.getStudentCount());
        assertEquals(2, mgr.getStudents().length);
    }

    @Test
    void addStudent_beyondCapacity_doesNotIncrementCount() {
        StudentManager mgr = new StudentManager(1);
        TestStudent s1 = new TestStudent("Esi Boakye", 20, "esi@example.com");
        TestStudent s2 = new TestStudent("Yaw Adjei", 21, "yaw@example.com");

        mgr.addStudent(s1);
        mgr.addStudent(s2); // should not be added (prints message)

        assertEquals(1, mgr.getStudentCount(), "Count should remain at capacity");
        assertEquals(1, mgr.getStudents().length, "Returned copy should have capacity count");
        assertSame(s1, mgr.getStudents()[0], "First student should be s1");
    }

    @Test
    void findStudent_byId_isCaseInsensitive() {
        StudentManager mgr = new StudentManager(2);
        TestStudent s1 = new TestStudent("Akua Boateng", 22, "akua@example.com");
        TestStudent s2 = new TestStudent("Kojo Asare", 23, "kojo@example.com");
        mgr.addStudent(s1);
        mgr.addStudent(s2);

        String id1 = s1.getStudentId(); // auto-generated like STU001
        String id2Lower = s2.getStudentId().toLowerCase();

        assertSame(s1, mgr.findStudent(id1));
        assertSame(s2, mgr.findStudent(id2Lower), "Lookup should ignore case");
        assertNull(mgr.findStudent("STU999"), "Unknown ID should return null");
    }

    @Test
    void getStudents_returnsCopy_notBackingArray() {
        StudentManager mgr = new StudentManager(1);
        TestStudent s1 = new TestStudent("Efe Amponsah", 24, "efe@example.com");
        mgr.addStudent(s1);

        Student[] copy = mgr.getStudents();
        assertEquals(1, copy.length);
        assertSame(s1, copy[0]);


        copy[0] = null;
        assertSame(s1, mgr.getStudents()[0], "Manager should not be affected by external mutations");
    }

    @Test
    void averageClassGrade_isMeanOfStudentsAverages() {
        StudentManager mgr = new StudentManager(3);
        TestStudent s1 = new TestStudent("Student A", 18, "a@x.com");
        TestStudent s2 = new TestStudent("Student B", 18, "b@x.com");
        TestStudent s3 = new TestStudent("Student C", 18, "c@x.com");

        // Averages: 80, 60, 0 (no grades)
        s1.recordGrade(80);
        s2.recordGrade(60);
        // s3 no grades recorded

        mgr.addStudent(s1);
        mgr.addStudent(s2);
        mgr.addStudent(s3);

        double avg = mgr.getAverageClassGrade();
        assertEquals((80.0 + 60.0 + 0.0) / 3.0, avg, 0.0001);
    }
}
