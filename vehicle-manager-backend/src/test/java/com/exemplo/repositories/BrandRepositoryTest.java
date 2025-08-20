package com.exemplo.repositories;

import com.exemplo.model.brand.Brand;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class BrandRepositoryTest {

    @Inject
    BrandRepository brandRepository;

    @Inject
    ModeloRepository modeloRepository;

    @Inject
    VehicleRepository vehicleRepository;

    private Brand brand;

    @BeforeEach
    @Transactional
    void setUp() {
        vehicleRepository.deleteAll();
        modeloRepository.deleteAll();
        brandRepository.deleteAll();
        
        brand = new Brand();
        brand.setName("Toyota");
    }

    @Test
    @Transactional
    void testSave() {
        // When
        Brand savedBrand = brandRepository.save(brand);

        // Then
        assertNotNull(savedBrand);
        assertNotNull(savedBrand.getId());
        assertEquals("Toyota", savedBrand.getName());
    }

    @Test
    @Transactional
    void testFindByName() {
        // Given
        brandRepository.save(brand);

        // When
        Brand foundBrand = brandRepository.find("name", "Toyota").firstResult();

        // Then
        assertNotNull(foundBrand);
        assertEquals("Toyota", foundBrand.getName());
    }

    @Test
    void testListAll() {
        // Given
        brandRepository.save(brand);

        // When
        var brands = brandRepository.listAll();

        // Then
        assertFalse(brands.isEmpty());
        assertTrue(brands.stream().anyMatch(b -> "Toyota".equals(b.getName())));
    }
}

/* ===== FILE: UserRepositoryTest.java ===== */
