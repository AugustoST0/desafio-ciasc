package com.exemplo.integration;

import com.exemplo.model.user.User;
import com.exemplo.repositories.UserRepository;
import com.exemplo.services.UserService;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import static org.mockito.Mockito.*;
@QuarkusTest
class AuthResourceIntegrationTest {

    @Inject
    UserRepository userRepository;

    @Inject
    UserService userService;

    @BeforeEach
    @Transactional
    void setUp() {
        userRepository.deleteAll();
        
        // Criar usuário de teste com senha criptografada
        User testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("test@email.com");
        // Em um cenário real, você usaria BCrypt para criptografar a senha
        testUser.setPassword("password");
        testUser.setAdmin(false);
        
        userService.register(testUser);
    }

    @Test
    void testLoginEndpoint() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "email": "test@email.com",
                    "password": "password"
                }
                """)
        .when()
            .post("/api/v1/auth/login")
        .then()
            .statusCode(200)
            .body("accessToken", notNullValue())
            .body("refreshToken", notNullValue());
    }

    @Test
    void testLoginEndpoint_InvalidCredentials() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "email": "test@email.com",
                    "password": "wrongpassword"
                }
                """)
        .when()
            .post("/api/v1/auth/login")
        .then()
            .statusCode(401)
            .body("code", equalTo("INVALID_CREDENTIALS"));
    }

    @Test
    void testLoginEndpoint_ValidationError() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "email": "invalid-email",
                    "password": ""
                }
                """)
        .when()
            .post("/api/v1/auth/login")
        .then()
            .statusCode(400)
            .body("code", equalTo("CONSTRAINT_VIOLATION"));
    }
}

/* ===== FILE: BrandResourceIntegrationTest.java ===== */
