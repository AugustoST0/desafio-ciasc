package com.exemplo.model.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testUserCreation() {
        // Given & When
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@email.com");
        user.setPassword("password");
        user.setAdmin(true);

        // Then
        assertEquals(1L, user.getId());
        assertEquals("Test User", user.getName());
        assertEquals("test@email.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertTrue(user.isAdmin());
    }

    @Test
    void testUserDefaults() {
        // Given & When
        User newUser = new User();

        // Then
        assertNull(newUser.getId());
        assertNull(newUser.getName());
        assertNull(newUser.getEmail());
        assertNull(newUser.getPassword());
        assertFalse(newUser.isAdmin());
    }

    @Test
    void testAllArgsConstructor() {
        // Given & When
        User user = new User(1L, "Test User", "test@email.com", "password", false);

        // Then
        assertEquals(1L, user.getId());
        assertEquals("Test User", user.getName());
        assertEquals("test@email.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertFalse(user.isAdmin());
    }
}

/* ===== PACKAGE: src/test/java/com/exemplo/security ===== */

/* ===== FILE: JWTTokenProviderTest.java ===== */
