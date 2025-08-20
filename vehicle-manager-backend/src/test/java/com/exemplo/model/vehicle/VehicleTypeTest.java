package com.exemplo.model.vehicle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTypeTest {

    @Test
    void testVehicleTypeCodes() {
        assertEquals(0, VehicleType.CARRO.getCode());
        assertEquals(1, VehicleType.MOTO.getCode());
        assertEquals(2, VehicleType.CAMINHAO.getCode());
        assertEquals(3, VehicleType.ONIBUS.getCode());
    }

    @Test
    void testVehicleTypeValues() {
        VehicleType[] types = VehicleType.values();
        assertEquals(4, types.length);
        assertEquals(VehicleType.CARRO, types[0]);
        assertEquals(VehicleType.MOTO, types[1]);
        assertEquals(VehicleType.CAMINHAO, types[2]);
        assertEquals(VehicleType.ONIBUS, types[3]);
    }
}

/* ===== FILE: UserTest.java ===== */
