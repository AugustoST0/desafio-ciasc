package com.exemplo.repositories;

import com.exemplo.model.user.User;
import com.exemplo.model.vehicle.Vehicle;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class VehicleRepository implements PanacheRepository<Vehicle> {

    @Transactional
    public Vehicle save(Vehicle vehicle) {
        persistAndFlush(vehicle);
        return vehicle;
    }
}
