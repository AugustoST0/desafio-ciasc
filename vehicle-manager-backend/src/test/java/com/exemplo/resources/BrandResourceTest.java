package com.exemplo.resources;

import com.exemplo.model.brand.Brand;
import com.exemplo.services.BrandService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrandResourceTest {

    @Mock
    private BrandService brandService;

    @InjectMocks
    private BrandResource brandResource;

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
        when(brandService.getAll()).thenReturn(brands);

        // When
        Response response = brandResource.getAll();

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        @SuppressWarnings("unchecked")
        List<Brand> result = (List<Brand>) response.getEntity();
        assertEquals(1, result.size());
        assertEquals("Toyota", result.get(0).getName());
        verify(brandService).getAll();
    }

    @Test
    void testGetById() {
        // Given
        when(brandService.getById(1L)).thenReturn(brand);

        // When
        Response response = brandResource.getById(1L);

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Brand result = (Brand) response.getEntity();
        assertEquals("Toyota", result.getName());
        verify(brandService).getById(1L);
    }

    @Test
    void testInsert() {
        // Given
        when(brandService.insert(brand)).thenReturn(brand);

        // When
        Response response = brandResource.insert(brand);

        // Then
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        Brand result = (Brand) response.getEntity();
        assertEquals("Toyota", result.getName());
        verify(brandService).insert(brand);
    }

    @Test
    void testUpdate() {
        // Given
        Brand updatedBrand = new Brand();
        updatedBrand.setName("Honda");
        when(brandService.update(1L, updatedBrand)).thenReturn(updatedBrand);

        // When
        Response response = brandResource.update(1L, updatedBrand);

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Brand result = (Brand) response.getEntity();
        assertEquals("Honda", result.getName());
        verify(brandService).update(1L, updatedBrand);
    }

    @Test
    void testDelete() {
        // Given
        doNothing().when(brandService).delete(1L);

        // When
        Response response = brandResource.delete(1L);

        // Then
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
        verify(brandService).delete(1L);
    }
}

/* ===== PACKAGE: src/test/java/com/exemplo/model ===== */

/* ===== FILE: VehicleTest.java ===== */
