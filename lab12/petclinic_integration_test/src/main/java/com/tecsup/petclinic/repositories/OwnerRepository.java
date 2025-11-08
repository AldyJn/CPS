package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Owner entity
 */
@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    List<Owner> findByFirstName(String firstName);

    List<Owner> findByLastName(String lastName);

    List<Owner> findByCity(String city);

    @Override
    List<Owner> findAll();
}
