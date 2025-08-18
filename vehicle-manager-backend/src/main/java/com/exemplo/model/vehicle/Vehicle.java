package com.exemplo.model.vehicle;

import com.exemplo.model.modelo.Modelo;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "vehicles", uniqueConstraints = {
        @UniqueConstraint(name = "uk_vehicles_plate", columnNames = "plate")
})
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    @NotNull(message = "O modelo do veículo é obrigatório")
    private Modelo model;

    @NotNull(message = "O ano do veículo é obrigatório")
    @Min(value = 1900, message = "Ano inválido")
    @Max(value = 2100, message = "Ano inválido")
    private int year;

    @NotBlank(message = "A placa do veículo é obrigatória")
    @Pattern(
            regexp = "^[A-Z]{3}[0-9][A-Z][0-9]{2}$",
            message = "Placa inválida. Formato esperado: ABC1D23"
    )
    @Column(nullable = false, unique = true)
    private String plate;

    @NotNull(message = "O tipo do veículo é obrigatório")
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @PrePersist
    @PreUpdate
    public void formatPlate() {
        if (this.plate != null) {
            this.plate = this.plate.toUpperCase();
        }
    }
}
