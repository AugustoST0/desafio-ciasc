package com.exemplo.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.exemplo.exception.exceptions.RegisterNotFoundException;
import com.exemplo.model.user.User;
import com.exemplo.model.user.UserUpdateResponseDTO;
import com.exemplo.repositories.UserRepository;
import com.exemplo.security.JWTTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JWTTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@email.com");
        user.setPassword("hashedpassword");
        user.setAdmin(false);
    }

    @Test
    void testGetAll() {
        // Given
        List<User> users = Arrays.asList(user);
        when(userRepository.listAll()).thenReturn(users);

        // When
        List<User> result = userService.getAll();

        // Then
        assertEquals(1, result.size());
        assertEquals("Test User", result.get(0).getName());
        verify(userRepository).listAll();
    }

    @Test
    void testGetById_Success() {
        // Given
        when(userRepository.findById(1L)).thenReturn(user);

        // When
        User result = userService.getById(1L);

        // Then
        assertNotNull(result);
        assertEquals("Test User", result.getName());
        verify(userRepository).findById(1L);
    }

    @Test
    void testGetById_NotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(null);

        // When & Then
        assertThrows(RegisterNotFoundException.class, () -> 
            userService.getById(1L));
    }

    @Test
    void testRegister() {
        // Given
        User newUser = new User();
        newUser.setName("New User");
        newUser.setEmail("new@email.com");
        newUser.setPassword("password");
        
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        try (MockedStatic<BCrypt> mockedBCrypt = Mockito.mockStatic(BCrypt.class)) {
            BCrypt.Hasher hasher = mock(BCrypt.Hasher.class);
            mockedBCrypt.when(BCrypt::withDefaults).thenReturn(hasher);
            when(hasher.hashToString(eq(12), any(char[].class))).thenReturn("hashedpassword");

            // When
            User result = userService.register(newUser);

            // Then
            assertNotNull(result);
            assertEquals("hashedpassword", result.getPassword());
            verify(userRepository).save(newUser);
        }
    }

    @Test
    void testUpdate() {
        // Given
        User updatedUser = new User();
        updatedUser.setName("Updated Name");
        updatedUser.setEmail("updated@email.com");
        
        when(userRepository.findById(1L)).thenReturn(user);
        when(jwtTokenProvider.generateAccessToken("updated@email.com")).thenReturn("access_token");
        when(jwtTokenProvider.generateRefreshToken("updated@email.com")).thenReturn("refresh_token");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        UserUpdateResponseDTO result = userService.update(1L, updatedUser);

        // Then
        assertNotNull(result);
        assertEquals("Updated Name", result.user().getName());
        assertEquals("updated@email.com", result.user().getEmail());
        assertEquals("access_token", result.accessToken());
        assertEquals("refresh_token", result.refreshToken());
    }

    @Test
    void testDelete() {
        // Given
        when(userRepository.findById(1L)).thenReturn(user);

        // When
        userService.delete(1L);

        // Then
        verify(userRepository).findById(1L);
        verify(userRepository).deleteById(1L);
    }
}

