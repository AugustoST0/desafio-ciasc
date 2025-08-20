package com.exemplo.model.vehicle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        vehicle = new Vehicle();
    }

    @Test
    void testFormatPlate_PrePersist() {
        // Given
        vehicle.setPlate("abc1d23");

        // When
        vehicle.formatPlate();

        // Then
        assertEquals("ABC1D23", vehicle.getPlate());
    }

    @Test
    void testFormatPlate_NullPlate() {
        // Given
        vehicle.setPlate(null);

        // When
        vehicle.formatPlate();

        // Then
        assertNull(vehicle.getPlate());
    }

    @Test
    void testFormatPlate_AlreadyUpperCase() {
        // Given
        vehicle.setPlate("ABC1D23");

        // When
        vehicle.formatPlate();

        // Then
        assertEquals("ABC1D23", vehicle.getPlate());
    }
}

/* ===== FILE: VehicleTypeTest.java ===== */
