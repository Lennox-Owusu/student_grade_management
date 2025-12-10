
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
//
//    @Test
//    void rejectsNullOrBlankValues_ifValidatedInConstructor() {
//        // test passed-rejected null entries
//        assertThrows(IllegalArgumentException.class, () -> new ElectiveSubject(null, "ART201"));
//        assertThrows(IllegalArgumentException.class, () -> new ElectiveSubject("Visual Arts", null));
//        assertThrows(IllegalArgumentException.class, () -> new ElectiveSubject("", "ART201"));
//        assertThrows(IllegalArgumentException.class, () -> new ElectiveSubject("Visual Arts", "   "));
//    }
}

