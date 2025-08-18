package com.exemplo.model.vehicle;

import com.exemplo.model.brand.Brand;
import com.exemplo.model.modelo.Modelo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    @NotNull
    private Modelo model;
    @NotNull
    private int year;
    @NotBlank
    private String plate;
    @NotNull
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
}
