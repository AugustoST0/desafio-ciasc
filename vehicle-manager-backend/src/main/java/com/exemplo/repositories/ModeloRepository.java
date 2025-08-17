package com.exemplo.repositories;

import com.exemplo.model.modelo.Modelo;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ModeloRepository implements PanacheRepository<Modelo> {

    @Transactional
    public Modelo save(Modelo modelo) {
        persistAndFlush(modelo);
        return modelo;
    }
}
