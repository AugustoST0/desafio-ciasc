package com.exemplo.exception.mappers;

import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.core.Response;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersistenceExceptionMapperTest {

    private PersistenceExceptionMapper mapper;

    @Mock
    private SQLException sqlException;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mapper = new PersistenceExceptionMapper();
    }

    @Test
    void testToResponse_EmailDuplicateEntry() {
        // Given
        ConstraintViolationException exception = new ConstraintViolationException("duplicate entry for email", sqlException, "email_constraint");

        // When
        Response response = mapper.toResponse(exception);

        // Then
        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
        ErrorResponse errorResponse = (ErrorResponse) response.getEntity();
        assertEquals("E-mail já cadastrado", errorResponse.message);
        assertEquals("EMAIL_ALREADY_EXISTS", errorResponse.code);
    }

    @Test
    void testToResponse_PlateDuplicateEntry() {
        // Given
        ConstraintViolationException exception = new ConstraintViolationException("duplicate entry for plate", sqlException, "plate_constraint");

        // When
        Response response = mapper.toResponse(exception);

        // Then
        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
        ErrorResponse errorResponse = (ErrorResponse) response.getEntity();
        assertEquals("Placa já cadastrada", errorResponse.message);
        assertEquals("PLATE_ALREADY_EXISTS", errorResponse.code);
    }

    @Test
    void testToResponse_BrandNameDuplicateEntry() {
        // Given
        ConstraintViolationException exception = new ConstraintViolationException("duplicate entry for name in brands", sqlException, "brands_name");

        // When
        Response response = mapper.toResponse(exception);

        // Then
        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
        ErrorResponse errorResponse = (ErrorResponse) response.getEntity();
        assertEquals("Nome da marca já cadastrado", errorResponse.message);
        assertEquals("BRAND_NAME_EXISTS", errorResponse.code);
    }

    @Test
    void testToResponse_ModelNameDuplicateEntry() {
        // Given
        ConstraintViolationException exception = new ConstraintViolationException("duplicate entry for name in models", sqlException, "models_name");

        // When
        Response response = mapper.toResponse(exception);

        // Then
        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
        ErrorResponse errorResponse = (ErrorResponse) response.getEntity();
        assertEquals("Nome do modelo já cadastrado", errorResponse.message);
        assertEquals("MODEL_NAME_EXISTS", errorResponse.code);
    }

    @Test
    void testToResponse_GenericPersistenceException() {
        // Given
        PersistenceException exception = new PersistenceException("Generic error");

        // When
        Response response = mapper.toResponse(exception);

        // Then
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        ErrorResponse errorResponse = (ErrorResponse) response.getEntity();
        assertEquals("Erro interno ao persistir dados", errorResponse.message);
        assertEquals("INTERNAL_ERROR", errorResponse.code);
    }
}

/* ===== PACKAGE: src/test/java/com/exemplo/services ===== */

/* ===== FILE: AuthServiceTest.java ===== */
