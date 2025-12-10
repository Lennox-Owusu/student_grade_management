
package org.testing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoreSubjectTest {

    @Test
    void constructsCoreSubject_withNameAndCode() {
        CoreSubject math = new CoreSubject("Mathematics", "MATH101");
        assertEquals("Mathematics", math.getSubjectName());
        assertEquals("MATH101", math.getSubjectCode());
    }

    @Test
    void rejectsNullOrBlankValues_ifValidatedInConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new CoreSubject(null, "MATH101"));
        assertThrows(IllegalArgumentException.class, () -> new CoreSubject("Mathematics", null));
        assertThrows(IllegalArgumentException.class, () -> new CoreSubject("", "MATH101"));
        assertThrows(IllegalArgumentException.class, () -> new CoreSubject("Mathematics", "  "));
    }
}

