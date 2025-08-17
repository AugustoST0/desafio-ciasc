package com.exemplo.resources;

import com.exemplo.model.modelo.Modelo;
import com.exemplo.services.ModeloService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/v1/modelos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ModeloResource {

    @Inject
    ModeloService modeloService;

    @GET
    @RolesAllowed("USER")
    public Response getAll() {
        List<Modelo> modelos = modeloService.getAll();
        return Response.ok(modelos).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("USER")
    public Response getById(@PathParam("id") Long id) {
        Modelo modelo = modeloService.getById(id);

        return Response.ok(modelo).build();
    }

    @POST
    @RolesAllowed("USER")
    public Response insert(Modelo modelo) {
        Modelo created = modeloService.insert(modelo);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("USER")
    public Response update(@PathParam("id") Long id, Modelo updatedModelo) {
        Modelo modelo = modeloService.update(id, updatedModelo);

        return Response.ok(modelo).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("USER")
    public Response delete(@PathParam("id") Long id) {
        modeloService.delete(id);
        return Response.noContent().build();
    }
}
