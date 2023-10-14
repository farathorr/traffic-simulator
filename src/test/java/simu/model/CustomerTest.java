package simu.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void addDestination() {
        Level level = new Level("Level");
        Customer customer = new Customer(level, 1, 1);
        customer.setDestinationX(24.2);
        customer.setDestinationY(56.3);
        assertEquals(24.2, customer.getDestinationX());
        assertEquals(56.3, customer.getDestinationY());
    }
}