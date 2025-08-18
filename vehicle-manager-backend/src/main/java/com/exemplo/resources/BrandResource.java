package com.exemplo.resources;

import com.exemplo.model.brand.Brand;
import com.exemplo.model.vehicle.Vehicle;
import com.exemplo.services.BrandService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/v1/brands")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class BrandResource {

    @Inject
    BrandService brandService;

    @GET
    @RolesAllowed("USER")
    public Response getAll() {
        List<Brand> brands = brandService.getAll();
        return Response.ok(brands).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("USER")
    public Response getById(@PathParam("id") Long id) {
        Brand brand = brandService.getById(id);

        return Response.ok(brand).build();
    }

    @POST
    @RolesAllowed("ADMIN")
    public Response insert(@Valid Brand brand) {
        Brand created = brandService.insert(brand);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response update(@PathParam("id") Long id, @Valid Brand updatedBrand) {
        Brand brand = brandService.update(id, updatedBrand);

        return Response.ok(brand).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADMIN")
    public Response delete(@PathParam("id") Long id) {
        brandService.delete(id);
        return Response.noContent().build();
    }
}
