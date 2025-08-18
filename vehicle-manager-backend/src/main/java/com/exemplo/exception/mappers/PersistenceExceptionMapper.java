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

        if (e instanceof ConstraintViolationException cve) {
            String msg = cve.getMessage().toLowerCase();

            if (msg.contains("duplicate entry")) {
                if (msg.contains("email")) {
                    return Response.status(Response.Status.CONFLICT)
                            .entity(new ErrorResponse("E-mail já cadastrado", "EMAIL_ALREADY_EXISTS"))
                            .build();
                }
                if (msg.contains("plate")) {
                    return Response.status(Response.Status.CONFLICT)
                            .entity(new ErrorResponse("Placa já cadastrada", "PLATE_ALREADY_EXISTS"))
                            .build();
                }
                if (msg.contains("name")) {
                    if (msg.contains("brands")) {
                        return Response.status(Response.Status.CONFLICT)
                                .entity(new ErrorResponse("Nome da marca já cadastrado", "BRAND_NAME_EXISTS"))
                                .build();
                    }
                    if (msg.contains("models")) {
                        return Response.status(Response.Status.CONFLICT)
                                .entity(new ErrorResponse("Nome do modelo já cadastrado", "MODEL_NAME_EXISTS"))
                                .build();
                    }
                }
            }
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Erro interno ao persistir dados", "INTERNAL_ERROR"))
                .build();
    }
}
