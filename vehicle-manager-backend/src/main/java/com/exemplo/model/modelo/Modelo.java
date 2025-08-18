package com.exemplo.model.modelo;

import com.exemplo.model.brand.Brand;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "models", uniqueConstraints = {
        @UniqueConstraint(name = "uk_moles_name", columnNames = "name")
})
public class Modelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "O nome do modelo é obrigatório")
    @Size(max = 100, message = "O nome do modelo não pode ter mais de 100 caracteres")
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    @NotNull(message = "A marca do modelo é obrigatória")
    private Brand brand;
}
