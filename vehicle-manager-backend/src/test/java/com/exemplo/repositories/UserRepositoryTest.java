package com.exemplo.repositories;

import com.exemplo.model.user.User;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserRepositoryTest {

    @Inject
    UserRepository userRepository;

    private User user;

    @BeforeEach
    @Transactional
    void setUp() {
        userRepository.deleteAll();
        
        user = new User();
        user.setName("Test User");
        user.setEmail("test@email.com");
        user.setPassword("hashedpassword");
        user.setAdmin(false);
    }

    @Test
    @Transactional
    void testSave() {
        // When
        User savedUser = userRepository.save(user);

        // Then
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals("Test User", savedUser.getName());
        assertEquals("test@email.com", savedUser.getEmail());
    }

    @Test
    @Transactional
    void testFindByEmail() {
        // Given
        userRepository.save(user);

        // When
        User foundUser = userRepository.find("email", "test@email.com").firstResult();

        // Then
        assertNotNull(foundUser);
        assertEquals("test@email.com", foundUser.getEmail());
        assertEquals("Test User", foundUser.getName());
    }

    @Test
    void testListAll() {
        // Given
        userRepository.save(user);

        // When
        var users = userRepository.listAll();

        // Then
        assertFalse(users.isEmpty());
        assertTrue(users.stream().anyMatch(u -> "test@email.com".equals(u.getEmail())));
    }
}

/* ===== PACKAGE: src/test/java/com/exemplo/integration ===== */

/* ===== FILE: AuthResourceIntegrationTest.java ===== */
