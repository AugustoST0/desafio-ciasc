package com.exemplo.security;

import com.exemplo.model.user.User;
import com.exemplo.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JWTTokenProviderTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private JWTTokenProvider jwtTokenProvider;

    private User user;
    private User adminUser;

    @BeforeEach
    void setUp() {
        // Configuração do issuer via reflection ou mock
        try {
            var field = JWTTokenProvider.class.getDeclaredField("issuer");
            field.setAccessible(true);
            field.set(jwtTokenProvider, "test-issuer");
        } catch (Exception e) {
            // Fallback para teste
        }

        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@email.com");
        user.setAdmin(false);

        adminUser = new User();
        adminUser.setId(2L);
        adminUser.setName("Admin User");
        adminUser.setEmail("admin@email.com");
        adminUser.setAdmin(true);
    }

    @Test
    void testGenerateAccessToken_RegularUser() {
        // Given
        when(userService.getUserByEmail("test@email.com")).thenReturn(user);

        // When
        String token = jwtTokenProvider.generateAccessToken("test@email.com");

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        verify(userService).getUserByEmail("test@email.com");
    }

    @Test
    void testGenerateAccessToken_AdminUser() {
        // Given
        when(userService.getUserByEmail("admin@email.com")).thenReturn(adminUser);

        // When
        String token = jwtTokenProvider.generateAccessToken("admin@email.com");

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        verify(userService).getUserByEmail("admin@email.com");
    }

    @Test
    void testGenerateRefreshToken() {
        // When
        String refreshToken = jwtTokenProvider.generateRefreshToken("test@email.com");

        // Then
        assertNotNull(refreshToken);
        assertFalse(refreshToken.isEmpty());
    }
}

