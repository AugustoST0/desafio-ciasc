package com.exemplo.resources;

import com.exemplo.model.user.LoginRequestDTO;
import com.exemplo.model.user.RefreshTokenRequestDTO;
import com.exemplo.model.user.TokenResponseDTO;
import com.exemplo.services.AuthService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/v1/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    public Response login(@Valid LoginRequestDTO dto) {
        TokenResponseDTO response = authService.authenticateUser(dto);
        return Response.ok(response).build();
    }

    @POST
    @Path("/refresh")
    public Response refreshToken(@Valid RefreshTokenRequestDTO dto) {
        TokenResponseDTO tokenResponse = authService.refreshTokens(dto.refreshToken());
        return Response.ok(tokenResponse).build();
    }
}
