package io.github.dribble312.common.utils.string;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.github.dribble312.common.utils.string.CaseUtils.*;

/**
 * @author czx
 */

public class CaseUtilsTest {

    /**
     * Test {@link CaseUtils#determineCase(String)}
     */
    @Test
    public void testDetermineCase() {
        Assertions.assertEquals(Case.UNKNOWN, determineCase(""));
        Assertions.assertEquals(Case.KEBAB, determineCase("kebab-test"));
        Assertions.assertEquals(Case.SNAKE, determineCase("snake_test"));
        Assertions.assertEquals(Case.CAPITAL_CAMEL, determineCase("CamelTest"));
        Assertions.assertEquals(Case.NORM_CAMEL, determineCase("camel"));
        Assertions.assertEquals(Case.NORM_CAMEL, determineCase("camelTest"));
        Assertions.assertEquals(Case.NORM_CAMEL, determineCase("cCamelTest"));

        Assertions.assertEquals(Case.UNKNOWN, determineCase("cCa-melTest"));
        Assertions.assertEquals(Case.UNKNOWN, determineCase("cCa_melTest"));
        Assertions.assertEquals(Case.UNKNOWN, determineCase("CC_=elTest"));
        Assertions.assertEquals(Case.UNKNOWN, determineCase("double__check"));
    }

    @Test
    public void testToCase() {
        Assertions.assertEquals("", upperCaseWord(""));
        Assertions.assertEquals("A", upperCaseWord("A"));
        Assertions.assertEquals("A", upperCaseWord("a"));
        Assertions.assertEquals("", lowercaseWord(""));
        Assertions.assertEquals("b", lowercaseWord("b"));
        Assertions.assertEquals("b", lowercaseWord("B"));

        Assertions.assertEquals("", toCase(Case.KEBAB, ""));
        Assertions.assertThrows(IllegalArgumentException.class, () -> toCase(Case.KEBAB, null));
        Assertions.assertEquals("aa-bb-cc", toCase(Case.KEBAB, "aaBbCc"));
        Assertions.assertEquals("aabbcc", toCase(Case.KEBAB, "aabbcc"));
        Assertions.assertThrows(IllegalArgumentException.class, ()->toCase(Case.KEBAB, "aa%bCc"));
        Assertions.assertEquals("aa-bb-cc", toCase(Case.KEBAB, "AaBbCc"));
        Assertions.assertEquals("aa-b-b-cc", toCase(Case.KEBAB, "aaBBCc"));
        Assertions.assertEquals("aab-b-cc", toCase(Case.KEBAB, "aab_b_cc"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> toCase(Case.KEBAB, "aab__cc"));
        Assertions.assertEquals("aabBCc", toCase(Case.NORM_CAMEL, "aab_b_cc"));
        Assertions.assertEquals("AabBCc", toCase(Case.CAPITAL_CAMEL, "aab_b_cc"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> toCase(Case.KEBAB, "a_aBBCc"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> toCase(Case.KEBAB, "A-aBBCc"));
    }

}
