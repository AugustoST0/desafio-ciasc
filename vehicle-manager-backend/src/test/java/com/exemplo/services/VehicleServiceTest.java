package com.exemplo.services;

import com.exemplo.exception.exceptions.RegisterNotFoundException;
import com.exemplo.model.brand.Brand;
import com.exemplo.model.modelo.Modelo;
import com.exemplo.model.vehicle.Vehicle;
import com.exemplo.model.vehicle.VehicleType;
import com.exemplo.repositories.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    private Vehicle vehicle;
    private Modelo modelo;
    private Brand brand;

    @BeforeEach
    void setUp() {
        brand = new Brand();
        brand.setId(1);
        brand.setName("Toyota");

        modelo = new Modelo();
        modelo.setId(1);
        modelo.setName("Corolla");
        modelo.setBrand(brand);

        vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setModel(modelo);
        vehicle.setYear(2023);
        vehicle.setPlate("ABC1D23");
        vehicle.setVehicleType(VehicleType.CARRO);
    }

    @Test
    void testGetAll() {
        // Given
        List<Vehicle> vehicles = Arrays.asList(vehicle);
        when(vehicleRepository.listAll()).thenReturn(vehicles);

        // When
        List<Vehicle> result = vehicleService.getAll();

        // Then
        assertEquals(1, result.size());
        assertEquals("ABC1D23", result.get(0).getPlate());
        verify(vehicleRepository).listAll();
    }

    @Test
    void testGetById_Success() {
        // Given
        when(vehicleRepository.findByIdOptional(1L)).thenReturn(Optional.of(vehicle));

        // When
        Vehicle result = vehicleService.getById(1L);

        // Then
        assertNotNull(result);
        assertEquals("ABC1D23", result.getPlate());
        verify(vehicleRepository).findByIdOptional(1L);
    }

    @Test
    void testGetById_NotFound() {
        // Given
        when(vehicleRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RegisterNotFoundException.class, () -> 
            vehicleService.getById(1L));
    }

    @Test
    void testInsert() {
        // Given
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

        // When
        Vehicle result = vehicleService.insert(vehicle);

        // Then
        assertNotNull(result);
        assertEquals("ABC1D23", result.getPlate());
        verify(vehicleRepository).save(vehicle);
    }

    @Test
    void testUpdate() {
        // Given
        Vehicle updatedVehicle = new Vehicle();
        updatedVehicle.setModel(modelo);
        updatedVehicle.setYear(2024);
        updatedVehicle.setPlate("XYZ9Z99");
        updatedVehicle.setVehicleType(VehicleType.MOTO);
        
        when(vehicleRepository.findByIdOptional(1L)).thenReturn(Optional.of(vehicle));

        // When
        Vehicle result = vehicleService.update(1L, updatedVehicle);

        // Then
        assertNotNull(result);
        assertEquals(2024, result.getYear());
        assertEquals("XYZ9Z99", result.getPlate());
        assertEquals(VehicleType.MOTO, result.getVehicleType());
        verify(vehicleRepository).findByIdOptional(1L);
    }

    @Test
    void testDelete() {
        // Given
        when(vehicleRepository.findByIdOptional(1L)).thenReturn(Optional.of(vehicle));

        // When
        vehicleService.delete(1L);

        // Then
        verify(vehicleRepository).findByIdOptional(1L);
        verify(vehicleRepository).deleteById(1L);
    }
}

/* ===== PACKAGE: src/test/java/com/exemplo/resources ===== */

/* ===== FILE: AuthResourceTest.java ===== */
