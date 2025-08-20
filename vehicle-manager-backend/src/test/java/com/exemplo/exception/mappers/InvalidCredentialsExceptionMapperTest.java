package com.exemplo.exception.mappers;

import com.exemplo.exception.exceptions.InvalidCredentialsException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidCredentialsExceptionMapperTest {

    private InvalidCredentialsExceptionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new InvalidCredentialsExceptionMapper();
    }

    @Test
    void testToResponse() {
        // Given
        InvalidCredentialsException exception = new InvalidCredentialsException("Invalid credentials");

        // When
        Response response = mapper.toResponse(exception);

        // Then
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        ErrorResponse errorResponse = (ErrorResponse) response.getEntity();
        assertEquals("Invalid credentials", errorResponse.message);
        assertEquals("INVALID_CREDENTIALS", errorResponse.code);
        assertNull(errorResponse.details);
    }
}

/* ===== FILE: PersistenceExceptionMapperTest.java ===== */
