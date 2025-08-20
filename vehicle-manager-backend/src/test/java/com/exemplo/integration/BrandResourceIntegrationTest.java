package com.exemplo.integration;

import com.exemplo.model.brand.Brand;
import com.exemplo.repositories.BrandRepository;
import com.exemplo.repositories.ModeloRepository;
import com.exemplo.repositories.VehicleRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import static org.mockito.Mockito.*;
@QuarkusTest
class BrandResourceIntegrationTest {

    @Inject
    BrandRepository brandRepository;

    @Inject
    ModeloRepository modeloRepository;

    @Inject
    VehicleRepository vehicleRepository;

    @BeforeEach
    @Transactional
    void setUp() {
        vehicleRepository.deleteAll();
        modeloRepository.deleteAll();
        brandRepository.deleteAll();
        
        Brand brand = new Brand();
        brand.setName("Toyota");
        brandRepository.save(brand);
    }

    @Test
    @TestSecurity(user = "testuser", roles = "USER")
    void testGetAllBrands() {
        given()
        .when()
            .get("/api/v1/brands")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0))
            .body("[0].name", equalTo("Toyota"));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"USER", "ADMIN"})
    void testCreateBrand() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "name": "Honda"
                }
                """)
        .when()
            .post("/api/v1/brands")
        .then()
            .statusCode(201)
            .body("name", equalTo("Honda"));
    }

    @Test
    @TestSecurity(user = "user", roles = "USER")
    void testCreateBrand_Forbidden() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "name": "Honda"
                }
                """)
        .when()
            .post("/api/v1/brands")
        .then()
            .statusCode(403);
    }

    @Test
    void testGetBrands_Unauthorized() {
        given()
        .when()
            .get("/api/v1/brands")
        .then()
            .statusCode(401);
    }
}

/* ===== FILE: VehicleResourceIntegrationTest.java ===== */
