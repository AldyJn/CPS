package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.repositories.VetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for Vet entity
 */
@Service
@Slf4j
public class VetServiceImpl implements VetService {

    private final VetRepository vetRepository;

    public VetServiceImpl(VetRepository vetRepository) {
        this.vetRepository = vetRepository;
    }

    @Override
    public Vet create(Vet vet) {
        return vetRepository.save(vet);
    }

    @Override
    public Vet update(Vet vet) {
        return vetRepository.save(vet);
    }

    @Override
    public void delete(Integer id) throws VetNotFoundException {
        Vet vet = findById(id);
        vetRepository.delete(vet);
    }

    @Override
    public Vet findById(Integer id) throws VetNotFoundException {
        Optional<Vet> vet = vetRepository.findById(id);
        if (!vet.isPresent()) {
            throw new VetNotFoundException("Vet not found with id: " + id);
        }
        return vet.get();
    }

    @Override
    public List<Vet> findByFirstName(String firstName) {
        return vetRepository.findByFirstName(firstName);
    }

    @Override
    public List<Vet> findByLastName(String lastName) {
        return vetRepository.findByLastName(lastName);
    }

    @Override
    public List<Vet> findAll() {
        return vetRepository.findAll();
    }
}
