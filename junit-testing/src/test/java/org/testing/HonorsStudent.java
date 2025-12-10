
package org.testing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HonorsStudentTest {

    @Test
    void studentType_isHonorsStudent() {
        HonorsStudent hs = new HonorsStudent("Ama Owusu", 19, "ama@example.com", "0240000000");
        assertEquals("Honors Student", hs.getStudentType());
    }

    @Test
    void passingGrade_forHonors_isSixtyPercent() {
        HonorsStudent hs = new HonorsStudent("Kofi Mensah", 20, "kofi@example.com", "0240000001");
        assertEquals(60.0, hs.getPassingGrade(), 0.0001);
    }

    @Test
    void honorsEligible_whenAverageAbove85() {
        HonorsStudent hs = new HonorsStudent("Esi Boakye", 21, "esi@example.com", "0240000002");

        // Record valid grades whose average is 87.5
        assertTrue(hs.recordGrade(90.0));
        assertTrue(hs.recordGrade(85.0));

        assertTrue(hs.checkHonorsEligibility(),
                "Average 87.5 should be eligible for honors (>= 85)");
    }

    @Test
    void honorsNotEligible_whenAverageBelow85() {
        HonorsStudent hs = new HonorsStudent("Yaw Adjei", 22, "yaw@example.com", "0240000003");

        // Average 82.0
        assertTrue(hs.recordGrade(80.0));
        assertTrue(hs.recordGrade(84.0));

        assertFalse(hs.checkHonorsEligibility(),
                "Average 82.0 should NOT be eligible for honors (< 85)");
    }

    @Test
    void honorsEligibility_boundaryExactly85_isEligible() {
        HonorsStudent hs = new HonorsStudent("Akua Boateng", 23, "akua@example.com", "0240000004");

        // Average exactly 85.0
        assertTrue(hs.recordGrade(85.0));
        assertTrue(hs.recordGrade(85.0));

        assertTrue(hs.checkHonorsEligibility(),
                "Average exactly 85.0 should be eligible (>= 85)");
    }

    @Test
    void recordGrade_rejectsInvalidValues_ifStudentRecordGradeValidates() {
        HonorsStudent hs = new HonorsStudent("Kojo Asare", 25, "kojo@example.com", "0240000006");

        // If your Student.recordGrade(double) returns false for invalid inputs, these assertions will pass.
        // If instead it throws IllegalArgumentException, change these to assertThrows accordingly.
        assertFalse(hs.recordGrade(-1),   "Negative grade should be rejected");
        assertFalse(hs.recordGrade(101),  "Grade > 100 should be rejected");
        assertFalse(hs.recordGrade(Double.NaN), "NaN grade should be rejected");
    }

    @Test
    void averageCalculation_matchesRecordedValues() {
        HonorsStudent hs = new HonorsStudent("Kwesi Frimpong", 24, "kwesi@example.com", "0240000007");

        assertTrue(hs.recordGrade(70.0));
        assertTrue(hs.recordGrade(80.0));
        assertTrue(hs.recordGrade(90.0));

        assertEquals(80.0, hs.calculateAverageGrade(), 0.0001);
    }
}
