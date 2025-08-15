package com.exemplo.resources;

import com.exemplo.model.user.User;
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
        List<User> users = userService.getAllUsers();
        return Response.ok(users).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        User user = userService.getUserById(id);
        return Response.ok(user).build();
    }

    @GET
    @Path("/byEmail")
    public Response getByEmail(@QueryParam("email") String email) {
        User user = userService.getUserByEmail(email);
        return Response.ok(user).build();
    }

    @POST
    @Path("/register")
    public Response register(@Valid User user) {
        User createdUser = userService.registerUser(user);
        return Response.status(Response.Status.CREATED).entity(createdUser).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("USER")
    public Response update(@PathParam("id") Long id, User user) {
        userService.updateUser(id, user);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("USER")
    public Response deleteById(@PathParam("id") Long id) {
        userService.deleteUserById(id);
        return Response.noContent().build();
    }
}
