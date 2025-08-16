package com.exemplo.services;

import com.exemplo.exception.exceptions.RegisterNotFoundException;
import com.exemplo.model.vehicle.Vehicle;
import com.exemplo.repositories.VehicleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class VehicleService {

    @Inject
    VehicleRepository vehicleRepository;

    public List<Vehicle> getAll() {
        return vehicleRepository.listAll();
    }

    public Vehicle getById(Long id) {
        return vehicleRepository.findByIdOptional(id)
                .orElseThrow(() -> new RegisterNotFoundException("Veículo não encontrado"));
    }

    @Transactional
    public Vehicle insert(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public Vehicle update(Long id, Vehicle updatedVehicle) {
        Vehicle vehicle = getById(id);
        vehicle.setBrand(updatedVehicle.getBrand());
        vehicle.setModel(updatedVehicle.getModel());
        vehicle.setYear(updatedVehicle.getYear());
        vehicle.setPlate(updatedVehicle.getPlate());
        vehicle.setType(updatedVehicle.getType());
        return vehicle;
    }

    @Transactional
    public void delete(Long id) {
        getById(id);
        vehicleRepository.deleteById(id);
    }
}
