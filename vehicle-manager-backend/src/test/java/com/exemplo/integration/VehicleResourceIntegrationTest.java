package com.exemplo.integration;

import com.exemplo.model.brand.Brand;
import com.exemplo.model.modelo.Modelo;
import com.exemplo.model.vehicle.Vehicle;
import com.exemplo.model.vehicle.VehicleType;
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

@QuarkusTest
class VehicleResourceIntegrationTest {

    @Inject
    VehicleRepository vehicleRepository;
    
    @Inject
    BrandRepository brandRepository;
    
    @Inject
    ModeloRepository modeloRepository;

    private Brand brand;
    private Modelo modelo;

    @BeforeEach
    @Transactional
    void setUp() {
        vehicleRepository.deleteAll();
        modeloRepository.deleteAll();
        brandRepository.deleteAll();
        
        // Criar marca
        brand = new Brand();
        brand.setName("Toyota");
        brandRepository.save(brand);
        
        // Criar modelo
        modelo = new Modelo();
        modelo.setName("Corolla");
        modelo.setBrand(brand);
        modeloRepository.save(modelo);
        
        // Criar veículo
        Vehicle vehicle = new Vehicle();
        vehicle.setModel(modelo);
        vehicle.setYear(2023);
        vehicle.setPlate("ABC1D23");
        vehicle.setVehicleType(VehicleType.CARRO);
        vehicleRepository.save(vehicle);
    }

    @Test
    @TestSecurity(user = "testuser", roles = "USER")
    void testGetAllVehicles() {
        given()
        .when()
            .get("/api/v1/vehicles")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0))
            .body("[0].plate", equalTo("ABC1D23"));
    }

    @Test
    @TestSecurity(user = "testuser", roles = "USER")
    void testCreateVehicle() {
        given()
            .contentType(ContentType.JSON)
            .body(String.format("""
                {
                    "model": {
                        "id": %d
                    },
                    "year": 2024,
                    "plate": "XYZ9Z99",
                    "vehicleType": "MOTO"
                }
                """, modelo.getId()))
        .when()
            .post("/api/v1/vehicles")
        .then()
            .statusCode(201)
            .body("plate", equalTo("XYZ9Z99"))
            .body("year", equalTo(2024))
            .body("vehicleType", equalTo("MOTO"));
    }

    @Test
    @TestSecurity(user = "testuser", roles = "USER")
    void testCreateVehicle_InvalidPlate() {
        given()
            .contentType(ContentType.JSON)
            .body(String.format("""
                {
                    "model": {
                        "id": %d
                    },
                    "year": 2024,
                    "plate": "INVALID",
                    "vehicleType": "CARRO"
                }
                """, modelo.getId()))
        .when()
            .post("/api/v1/vehicles")
        .then()
            .statusCode(400)
            .body("code", equalTo("CONSTRAINT_VIOLATION"));
    }

    @Test
    void testGetVehicles_Unauthorized() {
        given()
        .when()
            .get("/api/v1/vehicles")
        .then()
            .statusCode(401);
    }
}
