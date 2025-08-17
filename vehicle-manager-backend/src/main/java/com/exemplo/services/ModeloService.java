package com.exemplo.services;

import com.exemplo.exception.exceptions.RegisterNotFoundException;
import com.exemplo.model.modelo.Modelo;
import com.exemplo.repositories.ModeloRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ModeloService {

    @Inject
    ModeloRepository modeloRepository;

    public List<Modelo> getAll() {
        return modeloRepository.listAll();
    }

    public Modelo getById(Long id) {
        return modeloRepository.findByIdOptional(id)
                .orElseThrow(() -> new RegisterNotFoundException("Marca não encontrada"));
    }

    @Transactional
    public Modelo insert(Modelo modelo) {
        return modeloRepository.save(modelo);
    }

    @Transactional
    public Modelo update(Long id, Modelo updatedModelo) {
        Modelo modelo = getById(id);
        modelo.setName(updatedModelo.getName());
        modelo.setBrand(updatedModelo.getBrand());
        return modelo;
    }

    @Transactional
    public void delete(Long id) {
        getById(id);
        modeloRepository.deleteById(id);
    }
}
