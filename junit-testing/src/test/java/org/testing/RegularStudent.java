
package org.testing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegularStudentTest {

    @Test
    void studentType_isRegular() {
        RegularStudent s = new RegularStudent("Ama Owusu", 19, "ama@example.com", "0240000000");
        assertEquals("Regular Student", s.getStudentType());
    }

    @Test
    void passingGrade_isFiftyPercent() {
        RegularStudent s = new RegularStudent("Kofi Mensah", 20, "kofi@example.com", "0240000001");
        assertEquals(50.0, s.getPassingGrade(), 0.0001);
    }



    @Test
    void recordsValidGrade_andMarksFailWhenBelowThreshold() {
        RegularStudent s = new RegularStudent("Yaw Adjei", 22, "yaw@example.com", "0240000003");
        CoreSubject sci = new CoreSubject("Science", "SCI101");

        s.recordGrade(sci, 49.0); // below pass mark
        assertFalse(s.passed(sci), "49 should be failing");
    }

    @Test
    void averageCalculation_isCorrectAcrossMultipleGrades() {
        RegularStudent s = new RegularStudent("Akua Boateng", 23, "akua@example.com", "0240000004");
        CoreSubject eng = new CoreSubject("English", "ENG101");
        CoreSubject ict = new CoreSubject("ICT", "ICT101");

        s.recordGrade(eng, 80.0);
        s.recordGrade(ict, 60.0);

        assertEquals(70.0, s.calculateAverageGrade(), 70);
    }


}
