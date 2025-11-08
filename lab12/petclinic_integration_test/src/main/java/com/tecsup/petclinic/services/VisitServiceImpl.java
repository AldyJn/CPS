package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import com.tecsup.petclinic.repositories.VisitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for Visit entity
 */
@Service
@Slf4j
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;

    public VisitServiceImpl(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    @Override
    public Visit create(Visit visit) {
        return visitRepository.save(visit);
    }

    @Override
    public Visit update(Visit visit) {
        return visitRepository.save(visit);
    }

    @Override
    public void delete(Long id) throws VisitNotFoundException {
        Visit visit = findById(id);
        visitRepository.delete(visit);
    }

    @Override
    public Visit findById(Long id) throws VisitNotFoundException {
        Optional<Visit> visit = visitRepository.findById(id);
        if (!visit.isPresent()) {
            throw new VisitNotFoundException("Visit not found with id: " + id);
        }
        return visit.get();
    }

    @Override
    public List<Visit> findByVisitDate(LocalDate visitDate) {
        return visitRepository.findByVisitDate(visitDate);
    }

    @Override
    public List<Visit> findAll() {
        return visitRepository.findAll();
    }
}
