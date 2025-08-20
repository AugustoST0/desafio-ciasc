package com.exemplo.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.exemplo.exception.exceptions.InvalidCredentialsException;
import com.exemplo.exception.exceptions.InvalidRefreshTokenException;
import com.exemplo.model.user.*;
import com.exemplo.security.JWTTokenProvider;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JWTTokenProvider jwtTokenProvider;

    @Mock
    private JWTParser jwtParser;

    @Mock
    private JsonWebToken jsonWebToken;

    @InjectMocks
    private AuthService authService;

    private User user;
    private LoginRequestDTO loginDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@email.com");
        user.setPassword(BCrypt.withDefaults().hashToString(12, "password".toCharArray()));
        
        loginDto = new LoginRequestDTO("test@email.com", "password");
    }

    @Test
    void testAuthenticateUser_Success() {
        // Given
        when(userService.getUserByEmail("test@email.com")).thenReturn(user);
        when(jwtTokenProvider.generateAccessToken("test@email.com")).thenReturn("access_token");
        when(jwtTokenProvider.generateRefreshToken("test@email.com")).thenReturn("refresh_token");

        // When
        TokenResponseDTO result = authService.authenticateUser(loginDto);

        // Then
        assertNotNull(result);
        assertEquals("access_token", result.accessToken());
        assertEquals("refresh_token", result.refreshToken());
        verify(userService).getUserByEmail("test@email.com");
        verify(jwtTokenProvider).generateAccessToken("test@email.com");
        verify(jwtTokenProvider).generateRefreshToken("test@email.com");
    }

    @Test
    void testAuthenticateUser_InvalidPassword() {
        // Given
        LoginRequestDTO invalidLoginDto = new LoginRequestDTO("test@email.com", "wrongpassword");
        when(userService.getUserByEmail("test@email.com")).thenReturn(user);

        // When & Then
        assertThrows(InvalidCredentialsException.class, () -> 
            authService.authenticateUser(invalidLoginDto));
    }

    @Test
    void testRefreshTokens_Success() throws ParseException {
        // Given
        String refreshToken = "valid_refresh_token";
        when(jwtParser.parse(refreshToken)).thenReturn(jsonWebToken);
        when(jsonWebToken.getSubject()).thenReturn("test@email.com");
        when(userService.getUserByEmail("test@email.com")).thenReturn(user);
        when(jwtTokenProvider.generateAccessToken("test@email.com")).thenReturn("new_access_token");
        when(jwtTokenProvider.generateRefreshToken("test@email.com")).thenReturn("new_refresh_token");

        // When
        TokenResponseDTO result = authService.refreshTokens(refreshToken);

        // Then
        assertNotNull(result);
        assertEquals("new_access_token", result.accessToken());
        assertEquals("new_refresh_token", result.refreshToken());
    }

    @Test
    void testRefreshTokens_InvalidToken() throws ParseException {
        // Given
        String invalidToken = "invalid_token";
        when(jwtParser.parse(invalidToken)).thenThrow(new ParseException("Invalid token"));

        // When & Then
        assertThrows(InvalidRefreshTokenException.class, () -> 
            authService.refreshTokens(invalidToken));
    }
}

