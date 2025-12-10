
package org.testing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class GradeTest {

    @Test
    void constructsGrade_andSetsFields() {
        Subject math = new CoreSubject("Mathematics", "MATH101");
        Grade g = new Grade("S-001", math, 75.5);

        assertEquals("S-001", g.getStudentId());
        assertEquals("Mathematics", g.getSubject().getSubjectName());
        assertEquals("MATH101", g.getSubject().getSubjectCode());
        assertEquals(75.5, g.getGrade(), 0.0001);
        assertEquals(LocalDate.now().toString(), g.getDate(), "Date should default to today's ISO date");
        assertNotNull(g.getGradeId(), "gradeId should be generated");
        assertTrue(g.getGradeId().startsWith("GRD"), "gradeId should start with 'GRD'");
    }

    @Test
    void gradeId_isUniqueAndIncrementing() {
        Subject sci = new CoreSubject("Science", "SCI101");
        Grade g1 = new Grade("S-002", sci, 80.0);
        Grade g2 = new Grade("S-003", sci, 90.0);

        assertNotEquals(g1.getGradeId(), g2.getGradeId(), "Each Grade should have a unique ID");
        int n1 = Integer.parseInt(g1.getGradeId().replace("GRD", ""));
        int n2 = Integer.parseInt(g2.getGradeId().replace("GRD", ""));
        assertEquals(n1 + 1, n2, "IDs should increment by 1");
    }

    @Test
    void acceptsBoundaryGrades_0and100() {
        Subject eng = new CoreSubject("English", "ENG101");
        Grade gMin = new Grade("S-010", eng, 0.0);
        Grade gMax = new Grade("S-011", eng, 100.0);

        assertEquals(0.0, gMin.getGrade(), 0.0001);
        assertEquals(100.0, gMax.getGrade(), 0.0001);
    }

    @Test
    void rejectsNegativeGrade() {
        Subject ict = new CoreSubject("ICT", "ICT101");
        assertThrows(IllegalArgumentException.class,
                () -> new Grade("S-020", ict, -1),
                "Negative grade should be rejected");
    }

    @Test
    void rejectsGradeAbove100() {
        Subject ict = new CoreSubject("ICT", "ICT101");
        assertThrows(IllegalArgumentException.class,
                () -> new Grade("S-021", ict, 101),
                "Grade > 100 should be rejected");
    }

    @Test
    void rejectsBlankStudentId() {
        Subject math = new CoreSubject("Mathematics", "MATH101");
        assertThrows(IllegalArgumentException.class, () -> new Grade("",   math, 50));
        assertThrows(IllegalArgumentException.class, () -> new Grade("   ", math, 50));
        assertThrows(IllegalArgumentException.class, () -> new Grade(null, math, 50));
    }

    @Test
    void rejectsNullSubject() {
        assertThrows(IllegalArgumentException.class, () -> new Grade("S-030", null, 60));
    }

    @Test
    void rejectsNaNGrade() {
        Subject sci = new CoreSubject("Science", "SCI101");
        assertThrows(IllegalArgumentException.class, () -> new Grade("S-031", sci, Double.NaN));
    }
}
