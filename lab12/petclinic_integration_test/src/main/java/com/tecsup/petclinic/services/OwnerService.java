package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;

import java.util.List;

/**
 * Service for Owner entity
 */
public interface OwnerService {

    Owner create(Owner owner);

    Owner update(Owner owner);

    void delete(Long id) throws OwnerNotFoundException;

    Owner findById(Long id) throws OwnerNotFoundException;

    List<Owner> findByFirstName(String firstName);

    List<Owner> findByLastName(String lastName);

    List<Owner> findByCity(String city);

    List<Owner> findAll();
}
