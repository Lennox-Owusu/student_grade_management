
package org.testing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ElectiveSubjectTest {

    @Test
    void constructsElectiveSubject_withNameAndCode() {
        ElectiveSubject art = new ElectiveSubject("Visual Arts", "ART201");
        assertEquals("Visual Arts", art.getSubjectName());
        assertEquals("ART201", art.getSubjectCode());
    }

    @Test
    void subjectType_isElective() {
        ElectiveSubject art = new ElectiveSubject("Visual Arts", "ART201");
        // Adjust expected if your implementation uses a different label
        assertEquals("Elective", art.getSubjectType());
    }

    @Test
    void rejectsNullOrBlankValues_viaBaseSubjectValidation() {
        assertThrows(IllegalArgumentException.class, () -> new ElectiveSubject(null, "ART201"));
        assertThrows(IllegalArgumentException.class, () -> new ElectiveSubject("Visual Arts", null));
        assertThrows(IllegalArgumentException.class, () -> new ElectiveSubject("", "ART201"));
        assertThrows(IllegalArgumentException.class, () -> new ElectiveSubject("Visual Arts", "   "));
    }
}
