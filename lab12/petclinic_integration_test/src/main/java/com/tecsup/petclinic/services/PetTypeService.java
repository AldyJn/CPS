package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.PetType;
import com.tecsup.petclinic.exceptions.PetTypeNotFoundException;

import java.util.List;

/**
 * Service for PetType entity
 */
public interface PetTypeService {

    PetType create(PetType petType);

    PetType update(PetType petType);

    void delete(Integer id) throws PetTypeNotFoundException;

    PetType findById(Integer id) throws PetTypeNotFoundException;

    List<PetType> findByName(String name);

    List<PetType> findAll();
}
