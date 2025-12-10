
package org.testing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GradeManagerTest {

    @Test
    void addGrade_and_getGradeAt_returnsSameObject() {
        GradeManager gm = new GradeManager(5);
        Subject math = new CoreSubject("Mathematics", "C1");
        Grade g = new Grade("STU001", math, 85.0);

        gm.addGrade(g);

        assertEquals(1, gm.getGradeCount());
        assertSame(g, gm.getGradeAt(0));
        assertNull(gm.getGradeAt(-1));
        assertNull(gm.getGradeAt(1)); // out-of-range
    }

    @Test
    void calculateCoreAverage_forSingleStudent() {
        GradeManager gm = new GradeManager(10);
        String sid = "STU001";

        Subject math = new CoreSubject("Mathematics", "C1");
        Subject sci  = new CoreSubject("Science",    "C2");
        Subject art  = new ElectiveSubject("Art",    "E1");

        gm.addGrade(new Grade(sid, math, 80.0));  // core
        gm.addGrade(new Grade(sid, sci,  70.0));  // core
        gm.addGrade(new Grade(sid, art,  60.0));  // elective, should not count in core avg

        double coreAvg = gm.calculateCoreAverage(sid);
        assertEquals((80.0 + 70.0) / 2.0, coreAvg, 0.0001);
    }

    @Test
    void calculateElectiveAverage_forSingleStudent() {
        GradeManager gm = new GradeManager(10);
        String sid = "STU002";

        Subject music = new ElectiveSubject("Music", "E2");
        Subject pe    = new ElectiveSubject("Physical Education", "E3");
        Subject eng   = new CoreSubject("English", "C3");

        gm.addGrade(new Grade(sid, music, 75.0)); // elective
        gm.addGrade(new Grade(sid, pe,    65.0)); // elective
        gm.addGrade(new Grade(sid, eng,   90.0)); // core, should not count in elective avg

        double elecAvg = gm.calculateElectiveAverage(sid);
        assertEquals((75.0 + 65.0) / 2.0, elecAvg, 0.0001);
    }

    @Test
    void calculateOverallAverage_forSingleStudent() {
        GradeManager gm = new GradeManager(10);
        String sid = "STU003";

        Subject math = new CoreSubject("Mathematics", "C1");
        Subject art  = new ElectiveSubject("Art", "E1");

        gm.addGrade(new Grade(sid, math, 85.0)); // core
        gm.addGrade(new Grade(sid, art,  65.0)); // elective

        double overallAvg = gm.calculateOverallAverage(sid);
        assertEquals((85.0 + 65.0) / 2.0, overallAvg, 0.0001);
    }

    @Test
    void averages_returnZero_whenNoGradesForStudent() {
        GradeManager gm = new GradeManager(10);
        String sid = "STU404"; // not present

        assertEquals(0.0, gm.calculateCoreAverage(sid), 0.0001);
        assertEquals(0.0, gm.calculateElectiveAverage(sid), 0.0001);
        assertEquals(0.0, gm.calculateOverallAverage(sid), 0.0001);
    }
}
