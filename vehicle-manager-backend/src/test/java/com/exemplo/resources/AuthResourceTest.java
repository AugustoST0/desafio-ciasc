package com.exemplo.resources;

import com.exemplo.model.user.LoginRequestDTO;
import com.exemplo.model.user.RefreshTokenRequestDTO;
import com.exemplo.model.user.TokenResponseDTO;
import com.exemplo.services.AuthService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthResourceTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthResource authResource;

    private LoginRequestDTO loginDto;
    private RefreshTokenRequestDTO refreshTokenDto;
    private TokenResponseDTO tokenResponse;

    @BeforeEach
    void setUp() {
        loginDto = new LoginRequestDTO("test@email.com", "password");
        refreshTokenDto = new RefreshTokenRequestDTO("refresh_token");
        tokenResponse = new TokenResponseDTO("access_token", "refresh_token");
    }

    @Test
    void testLogin() {
        // Given
        when(authService.authenticateUser(loginDto)).thenReturn(tokenResponse);

        // When
        Response response = authResource.login(loginDto);

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        TokenResponseDTO result = (TokenResponseDTO) response.getEntity();
        assertEquals("access_token", result.accessToken());
        assertEquals("refresh_token", result.refreshToken());
        verify(authService).authenticateUser(loginDto);
    }

    @Test
    void testRefreshToken() {
        // Given
        when(authService.refreshTokens("refresh_token")).thenReturn(tokenResponse);

        // When
        Response response = authResource.refreshToken(refreshTokenDto);

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        TokenResponseDTO result = (TokenResponseDTO) response.getEntity();
        assertEquals("access_token", result.accessToken());
        assertEquals("refresh_token", result.refreshToken());
        verify(authService).refreshTokens("refresh_token");
    }
}
