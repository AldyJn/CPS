package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for Visit entity
 */
@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    List<Visit> findByVisitDate(LocalDate visitDate);

    @Override
    List<Visit> findAll();
}
