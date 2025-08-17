package com.exemplo.services;

import com.exemplo.exception.exceptions.RegisterNotFoundException;
import com.exemplo.model.brand.Brand;
import com.exemplo.repositories.BrandRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class BrandService {

    @Inject
    BrandRepository brandRepository;

    public List<Brand> getAll() {
        return brandRepository.listAll();
    }

    public Brand getById(Long id) {
        return brandRepository.findByIdOptional(id)
                .orElseThrow(() -> new RegisterNotFoundException("Marca não encontrada"));
    }

    @Transactional
    public Brand insert(Brand brand) {
        return brandRepository.save(brand);
    }

    @Transactional
    public Brand update(Long id, Brand updatedBrand) {
        Brand brand = getById(id);
        brand.setName(updatedBrand.getName());
        return brand;
    }

    @Transactional
    public void delete(Long id) {
        getById(id);
        brandRepository.deleteById(id);
    }
}
