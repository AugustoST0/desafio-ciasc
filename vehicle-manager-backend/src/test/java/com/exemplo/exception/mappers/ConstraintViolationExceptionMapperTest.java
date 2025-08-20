package com.exemplo.exception.mappers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConstraintViolationExceptionMapperTest {

    private ConstraintViolationExceptionMapper mapper;
    
    @Mock
    private ConstraintViolation<Object> violation;
    
    @Mock
    private Path path;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mapper = new ConstraintViolationExceptionMapper();
    }

    @Test
    void testToResponse() {
        // Given
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("Validation error");
        when(path.toString()).thenReturn("field");
        
        Set<ConstraintViolation<Object>> violations = Set.of(violation);
        ConstraintViolationException exception = new ConstraintViolationException("Constraint violation", violations);

        // When
        Response response = mapper.toResponse(exception);

        // Then
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        ErrorResponse errorResponse = (ErrorResponse) response.getEntity();
        assertEquals("Constraint violation", errorResponse.message);
        assertEquals("CONSTRAINT_VIOLATION", errorResponse.code);
        assertEquals(1, errorResponse.details.size());
        assertEquals("field: Validation error", errorResponse.details.get(0));
    }
}

/* ===== FILE: InvalidCredentialsExceptionMapperTest.java ===== */
