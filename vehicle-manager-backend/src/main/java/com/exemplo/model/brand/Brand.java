package com.exemplo.model.brand;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "brands", uniqueConstraints = {
        @UniqueConstraint(name = "uk_brands_name", columnNames = "name")
})
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "O nome da marca é obrigatório")
    @Size(max = 100, message = "O nome da marca não pode ter mais de 100 caracteres")
    @Column(nullable = false, unique = true)
    private String name;
}
