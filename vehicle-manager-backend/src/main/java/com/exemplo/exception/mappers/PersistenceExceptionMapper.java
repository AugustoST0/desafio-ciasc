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

            // Caso de e-mail duplicado
            if (msg.contains("duplicate entry") && msg.contains("email")) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(new ErrorResponse("E-mail já cadastrado", "EMAIL_ALREADY_EXISTS"))
                        .build();
            }

            if (msg.contains("cannot delete") || msg.contains("foreign key constraint fails")) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(new ErrorResponse(
                                "Não é possível deletar este registro porque existem registros associados a ele",
                                "ASSOCIATED_RECORDS_EXIST"
                        ))
                        .build();
            }
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Erro interno ao persistir dados", "INTERNAL_ERROR"))
                .build();
    }
}
