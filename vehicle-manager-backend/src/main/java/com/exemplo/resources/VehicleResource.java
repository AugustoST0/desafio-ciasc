package com.exemplo.resources;

import com.exemplo.model.vehicle.Vehicle;
import com.exemplo.services.VehicleService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/v1/vehicles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class VehicleResource {

    @Inject
    VehicleService vehicleService;

    @GET
    @RolesAllowed("USER")
    public Response getAll() {
        List<Vehicle> vehicles = vehicleService.getAll();
        return Response.ok(vehicles).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("USER")
    public Response getById(@PathParam("id") Long id) {
        Vehicle vehicle = vehicleService.getById(id);

        return Response.ok(vehicle).build();
    }

    @POST
    @RolesAllowed("USER")
    public Response insert(Vehicle vehicle) {
        Vehicle created = vehicleService.insert(vehicle);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("USER")
    public Response update(@PathParam("id") Long id, Vehicle updatedVehicle) {
        Vehicle vehicle = vehicleService.update(id, updatedVehicle);

        return Response.ok(vehicle).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("USER")
    public Response delete(@PathParam("id") Long id) {
        vehicleService.delete(id);
        return Response.noContent().build();
    }
}
