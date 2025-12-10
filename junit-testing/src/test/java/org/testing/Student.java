
package org.testing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {


    static class TestStudent extends Student {
        private final double threshold;

        public TestStudent(String name, int age, String email, double passingThreshold) {
            super(name, age, email);
            this.threshold = passingThreshold;
        }

        @Override
        public String getStudentType() {
            return "TestStudent";
        }

        @Override
        public double getPassingGrade() {
            return threshold;
        }
    }

    @Test
    void recordGrade_acceptsValidBounds_0_and_100_and_returnsTrue() {
        TestStudent s = new TestStudent("Ama Owusu", 18, "ama@example.com", 50.0);

        assertTrue(s.recordGrade(0.0),  "0.0 should be valid");
        assertTrue(s.recordGrade(100.0), "100.0 should be valid");
        assertEquals(2, s.gradeCount, "Two grades recorded");
    }

    @Test
    void recordGrade_rejectsInvalidValues_returnsFalse() {
        TestStudent s = new TestStudent("Kofi Mensah", 19, "kofi@example.com", 50.0);

        assertFalse(s.recordGrade(-1.0),       "Negative grade should be rejected");
        assertFalse(s.recordGrade(101.0),      "Grade above 100 should be rejected");
        assertFalse(s.recordGrade(Double.NaN), "NaN grade should be rejected by validateGrade");
        assertEquals(0, s.gradeCount, "No invalid grades should be stored");
    }

    @Test
    void validateGrade_matchesRecordGradeBoundsLogic() {
        TestStudent s = new TestStudent("Esi Boakye", 20, "esi@example.com", 50.0);

        assertTrue(s.validateGrade(0.0));
        assertTrue(s.validateGrade(100.0));
        assertFalse(s.validateGrade(-0.01));
        assertFalse(s.validateGrade(100.01));
        assertFalse(s.validateGrade(Double.NaN));
    }

    @Test
    void calculateAverageGrade_isZeroWhenNoGrades() {
        TestStudent s = new TestStudent("Yaw Adjei", 21, "yaw@example.com", 50.0);

        assertEquals(0.0, s.calculateAverageGrade(), 0.0001);
        assertFalse(s.isPassing(), "With 0 average and threshold 50, should not be passing");
    }

    @Test
    void calculateAverageGrade_correctForMultipleGrades() {
        TestStudent s = new TestStudent("Akua Boateng", 22, "akua@example.com", 50.0);

        assertTrue(s.recordGrade(70.0));
        assertTrue(s.recordGrade(80.0));
        assertTrue(s.recordGrade(90.0));

        assertEquals(80.0, s.calculateAverageGrade(), 0.0001);
    }

    @Test
    void isPassing_usesThreshold_fromSubclass() {
        // Threshold 60%
        TestStudent s60 = new TestStudent("Kojo Asare", 23, "kojo@example.com", 60.0);
        s60.recordGrade(59.9);
        assertFalse(s60.isPassing(), "Avg 59.9 vs threshold 60.0 => not passing");
        s60.recordGrade(60.1);
        assertTrue(s60.isPassing(), "Avg (≈60) vs threshold 60.0 => passing");

        // Threshold 50%
        TestStudent s50 = new TestStudent("Kwesi Frimpong", 24, "kwesi@example.com", 50.0);
        s50.recordGrade(49.0);
        assertFalse(s50.isPassing(), "Avg 49 vs threshold 50 => not passing");
        s50.recordGrade(51.0);
        assertTrue(s50.isPassing(), "Avg (≈50) vs threshold 50 => passing");
    }

    @Test
    void enrollSubject_increasesEnrolledSubjectCount_onNonNullSubject() {
        TestStudent s = new TestStudent("Efe Amponsah", 25, "efe@example.com", 50.0);
        assertEquals(0, s.getEnrolledSubjectCount());

        Subject math = new CoreSubject("Mathematics", "MATH101");
        Subject art  = new ElectiveSubject("Art", "ART201");

        s.enrollSubject(math);
        s.enrollSubject(art);

        assertEquals(2, s.getEnrolledSubjectCount(), "Two distinct subject codes enrolled");
    }

    @Test
    void enrollSubject_ignoresNullSubject() {
        TestStudent s = new TestStudent("Nana K", 26, "nana@example.com", 50.0);
        s.enrollSubject(null);
        assertEquals(0, s.getEnrolledSubjectCount(), "Null subject should not be added");
    }

    @Test
    void recordGrade_respectsCapacity_limit50() {
        TestStudent s = new TestStudent("Capacity Test", 18, "cap@example.com", 50.0);

        // Fill up to capacity
        for (int i = 0; i < 50; i++) {
            assertTrue(s.recordGrade(60.0), "Should record within capacity");
        }
        assertEquals(50, s.gradeCount);


        assertFalse(s.recordGrade(60.0), "Should return false when capacity reached");
        assertEquals(50, s.gradeCount, "Count remains at capacity");
    }
}
