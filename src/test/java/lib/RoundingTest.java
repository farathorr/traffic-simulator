package lib;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoundingTest {

    @Test
    void toFixed() {
        assertEquals(1.33, Rounding.toFixed(1.33333333333, 2));
        assertEquals(1.3, Rounding.toFixed(1.3, 5));
        assertEquals(1.0, Rounding.toFixed(1.0, 0));

        assertEquals(6.0, Rounding.toFixed(5.5, 0));
        assertEquals(5.125, Rounding.toFixed(5.125, 3));
        assertEquals(5.13, Rounding.toFixed(5.125, 2));

        assertEquals(-123.55, Rounding.toFixed(-123.55, 2));
        assertEquals(-123.5, Rounding.toFixed(-123.55, 1));
        assertEquals(-123.6, Rounding.toFixed(-123.56, 1));
    }
}