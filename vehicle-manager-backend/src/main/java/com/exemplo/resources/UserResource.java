package com.exemplo.resources;

import com.exemplo.model.user.User;
import com.exemplo.model.user.UserUpdateResponseDTO;
import com.exemplo.services.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class UserResource {

    @Inject
    UserService userService;

    @GET
    public Response getAll() {
        List<User> users = userService.getAll();
        return Response.ok(users).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        User user = userService.getById(id);
        return Response.ok(user).build();
    }

    @POST
    @Path("/register")
    @RolesAllowed("ADMIN")
    public Response register(@Valid User user) {
        User createdUser = userService.register(user);
        return Response.status(Response.Status.CREATED).entity(createdUser).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("USER")
    public Response update(@PathParam("id") Long id, @Valid User updatedUser) {
        UserUpdateResponseDTO user = userService.update(id, updatedUser);
        return Response.ok(user).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("USER")
    public Response delete(@PathParam("id") Long id) {
        userService.delete(id);
        return Response.noContent().build();
    }
}
