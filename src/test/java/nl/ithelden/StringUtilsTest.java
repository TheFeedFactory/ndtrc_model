package nl.ithelden;

import nl.ithelden.model.util.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringUtilsTest {
    @Test
    void blankCountsAsEmpty() {
        Assertions.assertTrue(StringUtils.isEmpty(null));
        Assertions.assertTrue(StringUtils.isEmpty(""));
        Assertions.assertTrue(StringUtils.isEmpty(" \t\n"));
        Assertions.assertFalse(StringUtils.isEmpty(" x "));
    }

    @Test
    void isNotEmptyIsTheNegation() {
        Assertions.assertFalse(StringUtils.isNotEmpty(null));
        Assertions.assertFalse(StringUtils.isNotEmpty("  "));
        Assertions.assertTrue(StringUtils.isNotEmpty("x"));
    }
}
