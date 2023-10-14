package simu.framework;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClockTest {

    @Test
    void getInstanceTest() {
        Clock clock = Clock.getInstance();
        assertNotNull(clock);
        assertTrue(clock == Clock.getInstance());
    }

}