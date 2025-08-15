package com.exemplo.exception.mappers;

import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class PersistenceExceptionMapper implements ExceptionMapper<PersistenceException> {

    @Override
    public Response toResponse(PersistenceException e) {
        if (e instanceof ConstraintViolationException &&
                e.getMessage().toLowerCase().contains("duplicate entry") &&
                e.getMessage().toLowerCase().contains("email")) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorResponse("E-mail já cadastrado", "EMAIL_ALREADY_EXISTS"))
                    .build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Erro interno ao persistir dados", "INTERNAL_ERROR"))
                .build();
    }
}
