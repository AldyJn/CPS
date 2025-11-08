package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.PetType;
import com.tecsup.petclinic.exceptions.PetTypeNotFoundException;
import com.tecsup.petclinic.repositories.PetTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for PetType entity
 */
@Service
@Slf4j
public class PetTypeServiceImpl implements PetTypeService {

    private final PetTypeRepository petTypeRepository;

    public PetTypeServiceImpl(PetTypeRepository petTypeRepository) {
        this.petTypeRepository = petTypeRepository;
    }

    @Override
    public PetType create(PetType petType) {
        return petTypeRepository.save(petType);
    }

    @Override
    public PetType update(PetType petType) {
        return petTypeRepository.save(petType);
    }

    @Override
    public void delete(Integer id) throws PetTypeNotFoundException {
        PetType petType = findById(id);
        petTypeRepository.delete(petType);
    }

    @Override
    public PetType findById(Integer id) throws PetTypeNotFoundException {
        Optional<PetType> petType = petTypeRepository.findById(id);
        if (!petType.isPresent()) {
            throw new PetTypeNotFoundException("PetType not found with id: " + id);
        }
        return petType.get();
    }

    @Override
    public List<PetType> findByName(String name) {
        return petTypeRepository.findByName(name);
    }

    @Override
    public List<PetType> findAll() {
        return petTypeRepository.findAll();
    }
}
