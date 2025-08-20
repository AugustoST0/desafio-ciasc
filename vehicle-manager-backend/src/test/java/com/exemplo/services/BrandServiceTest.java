package com.exemplo.services;

import com.exemplo.exception.exceptions.RegisterNotFoundException;
import com.exemplo.model.brand.Brand;
import com.exemplo.repositories.BrandRepository;
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
class BrandServiceTest {

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private BrandService brandService;

    private Brand brand;

    @BeforeEach
    void setUp() {
        brand = new Brand();
        brand.setId(1);
        brand.setName("Toyota");
    }

    @Test
    void testGetAll() {
        // Given
        List<Brand> brands = Arrays.asList(brand);
        when(brandRepository.listAll()).thenReturn(brands);

        // When
        List<Brand> result = brandService.getAll();

        // Then
        assertEquals(1, result.size());
        assertEquals("Toyota", result.get(0).getName());
        verify(brandRepository).listAll();
    }

    @Test
    void testGetById_Success() {
        // Given
        when(brandRepository.findByIdOptional(1L)).thenReturn(Optional.of(brand));

        // When
        Brand result = brandService.getById(1L);

        // Then
        assertNotNull(result);
        assertEquals("Toyota", result.getName());
        verify(brandRepository).findByIdOptional(1L);
    }

    @Test
    void testGetById_NotFound() {
        // Given
        when(brandRepository.findByIdOptional(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RegisterNotFoundException.class, () -> 
            brandService.getById(1L));
    }

    @Test
    void testInsert() {
        // Given
        when(brandRepository.save(brand)).thenReturn(brand);

        // When
        Brand result = brandService.insert(brand);

        // Then
        assertNotNull(result);
        assertEquals("Toyota", result.getName());
        verify(brandRepository).save(brand);
    }

    @Test
    void testUpdate() {
        // Given
        Brand updatedBrand = new Brand();
        updatedBrand.setName("Honda");
        
        when(brandRepository.findByIdOptional(1L)).thenReturn(Optional.of(brand));

        // When
        Brand result = brandService.update(1L, updatedBrand);

        // Then
        assertNotNull(result);
        assertEquals("Honda", result.getName());
        verify(brandRepository).findByIdOptional(1L);
    }

    @Test
    void testDelete() {
        // Given
        when(brandRepository.findByIdOptional(1L)).thenReturn(Optional.of(brand));

        // When
        brandService.delete(1L);

        // Then
        verify(brandRepository).findByIdOptional(1L);
        verify(brandRepository).deleteById(1L);
    }
}

