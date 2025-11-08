package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;

import java.time.LocalDate;
import java.util.List;

/**
 * Service for Visit entity
 */
public interface VisitService {

    Visit create(Visit visit);

    Visit update(Visit visit);

    void delete(Long id) throws VisitNotFoundException;

    Visit findById(Long id) throws VisitNotFoundException;

    List<Visit> findByVisitDate(LocalDate visitDate);

    List<Visit> findAll();
}
