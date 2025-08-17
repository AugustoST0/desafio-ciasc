package com.exemplo.repositories;

import com.exemplo.model.brand.Brand;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class BrandRepository implements PanacheRepository<Brand> {

    @Transactional
    public Brand save(Brand brand) {
        persistAndFlush(brand);
        return brand;
    }
}
